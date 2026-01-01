package com.argueseye.service.controller;


import com.argueseye.service.DTO.ScanResultDTO;
import com.argueseye.service.DTO.UrlScanResponse;
import com.argueseye.service.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("urlscan")
public class UrlScanController {

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/health")
    public ResponseEntity<String> isRunning(){
        if (!rateLimiterService.allowHealthCheck()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate Exceeded");
        }
        return ResponseEntity.ok("Running: true");
    }



    @GetMapping()
    public ResponseEntity scanUrl(@RequestParam String url, @RequestHeader String deviceId){
        boolean status = rateLimiterService.allowRequest(deviceId);

        if(status){
            long tokens = rateLimiterService.getAvilableTokenes(deviceId);

           // UrlScanResponse urlScanResponse = urlScanService.callApi(url).block();

            UrlScanResponse  urlScanResponse = new UrlScanResponse();
            ScanResultDTO scanResultDTO = ScanResultDTO.from(urlScanResponse,url);

            return ResponseEntity.ok(
                    Map.of(
                            "status","Request Accepted",
                            "tokens-left",tokens,
                            "scan",scanResultDTO
                    )
            );
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate Exceeded");
    }
}
