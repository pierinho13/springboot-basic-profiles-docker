package com.pierinho13.apm.service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraceDemoService {

    private final RestTemplate restTemplate;

    @Value("${api.base-url:http://localhost:8081}")
    private String apiBaseUrl;

    public TraceDemoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> runTrace(int loops, Duration sleep, boolean fail) {
        long start = System.currentTimeMillis();

        // Llamar al microservicio externo (apm-examples-api)
        String traceUrl = apiBaseUrl + "/api/trace?loops=" + loops + "&sleepMs=" + sleep.toMillis() + "&fail=" + fail;

        @SuppressWarnings("unchecked")
        Map<String, Object> apiResponse = restTemplate.getForObject(traceUrl, Map.class);

        // También llamar a echo en el otro servicio
        String echoUrl = apiBaseUrl + "/api/echo?value=" + loops;
        String echo = restTemplate.getForObject(echoUrl, String.class);

        long ms = System.currentTimeMillis() - start;

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);
        out.put("apiResponse", apiResponse);
        out.put("echo", echo);
        out.put("totalTookMs", ms);
        return out;
    }

    public Map<String, Object> generateLoad(int requests, int concurrency) {
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, concurrency));
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < requests; i++) {
            final int n = i;
            futures.add(pool.submit(() -> restTemplate.getForObject(
                    apiBaseUrl + "/api/trace?loops=2&sleepMs=25&fail=" + (n % 15 == 0),
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
}
