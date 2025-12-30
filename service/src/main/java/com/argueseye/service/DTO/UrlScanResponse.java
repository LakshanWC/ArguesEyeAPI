package com.argueseye.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlScanResponse {
    String message;
    String uuid;
    String result;
    String api;
    String visibility;
    String url;
}
