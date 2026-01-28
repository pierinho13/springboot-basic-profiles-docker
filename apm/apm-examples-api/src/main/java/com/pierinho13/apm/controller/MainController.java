package com.pierinho13.apm.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	

    @GetMapping("/")
    public Map<String, Object> echo(@RequestParam(defaultValue = "OK") String value) {
        return Map.of(
                "value", value,
                "ts", System.currentTimeMillis()
        );
    }

}
