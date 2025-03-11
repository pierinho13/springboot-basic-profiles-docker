package com.pierinho13.helloworld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pierinho13.helloworld.config.CustomPropertyConfig;

@RestController
public class MainController {
	
	@Autowired

    private CustomPropertyConfig customPropertyConfig;

    @GetMapping("/")

    public String getMain() {
        return "Hello world the key is : " + customPropertyConfig.getKey();

    }

}
