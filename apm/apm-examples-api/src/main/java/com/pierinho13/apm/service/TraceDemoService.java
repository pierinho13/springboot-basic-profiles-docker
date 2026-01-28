package com.pierinho13.apm.service;

import java.time.Duration;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class TraceDemoService {

    private final DbTraceService dbTraceService;

    public TraceDemoService(DbTraceService dbTraceService) {
        this.dbTraceService = dbTraceService;
    }

    public Map<String, Object> runTrace(int loops, Duration sleep, boolean fail) {
        long start = System.currentTimeMillis();

        // CPU work
        String payload = cpuWork(loops);

        // I/O simulada
        sleep(sleep);

        // Query a H2 - esto generará spans de DB en APM
        Map<String, Object> dbResult = dbTraceService.writeAndRead("trace-" + loops, 2, 5);

        if (fail) {
            throw new IllegalStateException("Forced error to validate APM errors tab");
        }

        long ms = System.currentTimeMillis() - start;

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);
        out.put("loops", loops);
        out.put("sleepMs", sleep.toMillis());
        out.put("payloadHash", payload.hashCode());
        out.put("dbResult", dbResult);
        out.put("tookMs", ms);
        return out;
    }

    private String cpuWork(int loops) {
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
