package com.argueseye.service.service;

import com.argueseye.service.DTO.UrlScanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
public class UrlScanService {

    private final static String API_KEY = "";
    private final static String URLSCAN_API_ENDPOINT = "https://urlscan.io/api/v1/scan/";

    private final WebClient webClient;

    @Autowired
    public UrlScanService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public Mono<UrlScanResponse> callApi(String webUrl){
        return webClient.post()
                .uri(URLSCAN_API_ENDPOINT)
                .header("API-Key", API_KEY)
                .header("Content-Type","application/json")
                .bodyValue(Map.of("url",webUrl,"visibility","private"))
                .retrieve()
                .bodyToMono(UrlScanResponse.class);
    }
}
