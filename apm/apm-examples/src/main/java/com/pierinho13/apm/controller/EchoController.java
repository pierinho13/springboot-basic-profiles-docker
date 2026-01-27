package com.pierinho13.apm.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EchoController {

    @GetMapping("/echo")
    public Map<String, Object> echo(@RequestParam(defaultValue = "hello") String value) {
        return Map.of(
                "value", value,
                "ts", System.currentTimeMillis()
        );
    }
}
