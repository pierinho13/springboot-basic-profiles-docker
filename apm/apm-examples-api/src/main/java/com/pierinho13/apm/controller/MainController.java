package com.pierinho13.apm.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public Map<String, Object> index(@RequestParam(defaultValue = "OK") String value) {
        return Map.of(
                "value", value,
                "ts", System.currentTimeMillis()
        );
    }
}
