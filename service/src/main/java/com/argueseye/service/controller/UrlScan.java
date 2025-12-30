package com.argueseye.service.controller;


import com.argueseye.service.DTO.UrlScanResponse;
import com.argueseye.service.service.RateLimiterService;
import com.argueseye.service.service.UrlScanService;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@RestController
@RequestMapping("urlscan")
public class UrlScan {

    @Autowired
    UrlScanService urlScanService;


    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/health")
    public ResponseEntity<String> isRunning(){
        return new ResponseEntity<>("Running : true",HttpStatus.OK);
    }


    //temperay testing endpoint
    // provide all the tokens counts devices and there own respective tokens.
    //TODO
    // add token reset timers
    // show how much time left for the next reset
    // ...etc some other things

    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> getBucketDetails(){

        Map<String,Object> response = new HashMap<>();

        long globalTokens = rateLimiterService.getGlobalTokens();
        List<String> devices = rateLimiterService.bucketList();

        Map<String,Long> deviceToken = new HashMap<>();

        for(int i = 0; i<devices.size();i++){
            deviceToken.put(devices.get(i), rateLimiterService.getAvilableTokenes(devices.get(i)));
        }

        response.put("globalTokens",globalTokens);
        response.put("devices",deviceToken);

        return ResponseEntity.ok(response);
    }


    @GetMapping()
    public ResponseEntity scanUrl(@RequestParam String url, @RequestHeader String deviceId){
        boolean status = rateLimiterService.allowRequest(deviceId);

        if(status){
            long tokens = rateLimiterService.getAvilableTokenes(deviceId);

            UrlScanResponse urlScanResponse = urlScanService.callApi(url).block();

            return ResponseEntity.ok(
                    Map.of(
                            "message","Request Accepted",
                            "Tokensleft",tokens,
                            "scanResponse",urlScanResponse
                    )
            );


        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate Exceeded");
    }
}
