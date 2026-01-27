package com.pierinho13.apm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pierinho13.apm.config.CustomPropertyConfig;

@Controller
public class MainController {
	
    @Autowired
    private CustomPropertyConfig customPropertyConfig;
    
    @Autowired
    private Environment environment;

    @GetMapping("/")
    public String getMain(Model model) {
    	
    	Map<String, String> envVariables = System.getenv();
        
        model.addAttribute("envVariables",envVariables);
        
        Map<String, String> propertiesMap = new HashMap<>();


        for (PropertySource<?> propertySource : ((org.springframework.core.env.AbstractEnvironment) environment).getPropertySources()) {

            if (propertySource instanceof org.springframework.core.env.MapPropertySource) {

                Map<String, Object> source = ((org.springframework.core.env.MapPropertySource) propertySource).getSource();

                for (Map.Entry<String, Object> entry : source.entrySet()) {

                    propertiesMap.put(entry.getKey(), entry.getValue().toString());

                }

            }

        }

        model.addAttribute("propertiesMap", propertiesMap);
        
        model.addAttribute("customProperty", customPropertyConfig.getKey());
        
        return "greeting";
        
    }

}
