package com.argueseye.service.controller;

import com.argueseye.service.service.ManagementService;
import com.argueseye.service.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("management")
public class ManagementController {

    @Value("${DEBUG_API_KEY}")
    private String debugApiKey;

    @Autowired
    ManagementService managementService;

    @GetMapping(value = "/debug")
    public ResponseEntity<?> getBucketDetails(@RequestHeader("My-Api-Key") String apiKey){

        //Security check
        if(!debugApiKey.equals(apiKey)){
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(managementService.getServerDetails());
    }


    @GetMapping("/hello")
    public String tempMethod() {
        System.out.println("hello from server");
        return "Hello";
    }
}
