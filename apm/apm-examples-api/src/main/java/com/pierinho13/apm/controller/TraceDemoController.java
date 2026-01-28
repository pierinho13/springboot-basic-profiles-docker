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

    @GetMapping("/trace")
    public ResponseEntity<Map<String, Object>> trace(
            @RequestParam(defaultValue = "3") int loops,
            @RequestParam(defaultValue = "50") long sleepMs,
            @RequestParam(defaultValue = "false") boolean fail
    ) {
        return ResponseEntity.ok(traceDemoService.runTrace(loops, Duration.ofMillis(sleepMs), fail));
    }
}
