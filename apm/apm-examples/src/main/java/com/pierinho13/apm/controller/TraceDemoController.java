package com.pierinho13.apm.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pierinho13.apm.service.TraceDemoService;

@RestController
@RequestMapping("/api")
public class TraceDemoController {

    private final TraceDemoService traceDemoService;

    public TraceDemoController(TraceDemoService traceDemoService) {
        this.traceDemoService = traceDemoService;
    }

    // Genera una transacción con varios spans internos
    @GetMapping("/trace")
    public ResponseEntity<Map<String, Object>> trace(
            @RequestParam(defaultValue = "3") int loops,
            @RequestParam(defaultValue = "50") long sleepMs,
            @RequestParam(defaultValue = "false") boolean fail
    ) {
        return ResponseEntity.ok(traceDemoService.runTrace(loops, Duration.ofMillis(sleepMs), fail));
    }

    // Genera carga (muchas requests internas + concurrencia)
    @PostMapping("/load")
    public ResponseEntity<Map<String, Object>> load(
            @RequestParam(defaultValue = "20") int requests,
            @RequestParam(defaultValue = "5") int concurrency
    ) {
        return ResponseEntity.ok(traceDemoService.generateLoad(requests, concurrency));
    }
}
