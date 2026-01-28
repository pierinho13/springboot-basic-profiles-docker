package com.pierinho13.apm.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pierinho13.apm.service.DbTraceService;

@RestController
@RequestMapping("/api")
public class EchoController {

    private final DbTraceService dbTraceService;

    public EchoController(DbTraceService dbTraceService) {
        this.dbTraceService = dbTraceService;
    }

    @GetMapping("/echo")
    public Map<String, Object> echo(@RequestParam(defaultValue = "hello") String value) {
        // Query simulada a H2 - genera spans de DB en APM
        Map<String, Object> dbResult = dbTraceService.writeAndRead("echo-" + value, 1, 3);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("value", value);
        out.put("ts", System.currentTimeMillis());
        out.put("dbResult", dbResult);
        return out;
    }
}
