package com.pierinho13.apm.service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraceDemoService {

    private final RestTemplate restTemplate;

    public TraceDemoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> runTrace(int loops, Duration sleep, boolean fail) {
        long start = System.currentTimeMillis();

        // CPU work
        String payload = cpuWork(loops);

        // I/O simulada
        sleep(sleep);

        // HTTP client span (RestTemplate suele ser auto-instrumentado por APM)
        String echo = restTemplate.getForObject("http://localhost:8080/api/echo?value=" + loops, String.class);

        if (fail) {
            // Para ver errores en APM
            throw new IllegalStateException("Forced error to validate APM errors tab");
        }

        long ms = System.currentTimeMillis() - start;

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);
        out.put("loops", loops);
        out.put("sleepMs", sleep.toMillis());
        out.put("echo", echo);
        out.put("payloadHash", payload.hashCode());
        out.put("tookMs", ms);
        return out;
    }

    public Map<String, Object> generateLoad(int requests, int concurrency) {
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, concurrency));
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < requests; i++) {
            final int n = i;
            futures.add(pool.submit(() -> restTemplate.getForObject(
                    "http://localhost:8080/api/trace?loops=2&sleepMs=25&fail=" + (n % 15 == 0),
                    String.class
            )));
        }

        int ok = 0, failed = 0;
        for (Future<String> f : futures) {
            try {
                f.get(10, TimeUnit.SECONDS);
                ok++;
            } catch (Exception e) {
                failed++;
            }
        }

        pool.shutdown();

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("requested", requests);
        out.put("concurrency", concurrency);
        out.put("ok", ok);
        out.put("failed", failed);
        return out;
    }

    private String cpuWork(int loops) {
        // Algo simple que consume CPU y genera stacktrace interesante
        long acc = 0;
        for (int i = 0; i < loops * 50_000; i++) {
            acc += (i * 31L) ^ (acc >>> 1);
        }
        return "acc=" + acc;
    }

    private void sleep(Duration d) {
        try {
            Thread.sleep(Math.max(0, d.toMillis()));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
