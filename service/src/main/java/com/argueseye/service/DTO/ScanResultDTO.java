package com.argueseye.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanResultDTO {
    public String uuid;
    public String url;
    public String apiMessage;

    public static ScanResultDTO from(UrlScanResponse response,String fallbackUrl){
        ScanResultDTO dto = new ScanResultDTO();
        dto.uuid = (response.uuid != null) ? response.uuid : "N/A";
        dto.url = (response.url != null) ? response.url : fallbackUrl;
        dto.apiMessage = (response.message != null)? response.message : "N/A";
        return dto;
    }
}
