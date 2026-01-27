package com.pierinho13.apm.controller;

import com.pierinho13.apm.service.DbTraceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/db")
public class DbTraceController {

    private final DbTraceService dbTraceService;

    public DbTraceController(DbTraceService dbTraceService) {
        this.dbTraceService = dbTraceService;
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> run(
            @RequestParam(defaultValue = "demo") String type,
            @RequestParam(defaultValue = "10") int writes,
            @RequestParam(defaultValue = "5") int reads
    ) {
        return ResponseEntity.ok(dbTraceService.writeAndRead(type, writes, reads));
    }

    @PostMapping("/fail")
    public ResponseEntity<Map<String, Object>> fail() {
        dbTraceService.failDb();
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
