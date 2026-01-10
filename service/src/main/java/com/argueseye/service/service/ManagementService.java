package com.argueseye.service.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagementService {

    @Autowired
    RateLimiterService rateLimiterService;

    public Map<String, Object> getServerDetails(){
        Map<String, Object> response = new HashMap<>();

        try {

            //RateLimiter details
            long globalTokens = rateLimiterService.getGlobalTokens();
            List<String> devices = rateLimiterService.bucketList();

            Map<String, Long> deviceTokenDetails = new HashMap<>();
            for (int i = 0; i < devices.size(); i++) {
                deviceTokenDetails.put(devices.get(i), rateLimiterService.getAvilableTokenes(devices.get(i)));
            }

            //memory details
            Runtime runtime = Runtime.getRuntime();
            long heapUsedMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
            long heapMaxMb = runtime.maxMemory() / (1024 * 1024);

            //cpu load details
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            long totalPhysicalMemoryBytes = 0;
            long freePhysicalMemoryBytes = 0;
            double cpuLoadPercent = -1.0;

            if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
                totalPhysicalMemoryBytes = sunOsBean.getTotalPhysicalMemorySize();
                freePhysicalMemoryBytes = sunOsBean.getFreePhysicalMemorySize();
                cpuLoadPercent = sunOsBean.getSystemCpuLoad() * 100;
            }

            String cpuLoadDisplay = cpuLoadPercent >= 0
                    ? String.format("%.2f%%", cpuLoadPercent)
                    : "N/A (unsupported on this JVM/OS)";

            Map<String, Object> serverDetails = Map.of(
                    "heap-used-mb", heapUsedMb,
                    "heap-max-mb", heapMaxMb,
                    "system-total-ram-mb", totalPhysicalMemoryBytes,
                    "system-free-ram-mb", freePhysicalMemoryBytes,
                    "system-used-ram-mb", totalPhysicalMemoryBytes - freePhysicalMemoryBytes,
                    "cpu-load-percent", cpuLoadDisplay
            );


            response.put("global-tokens", globalTokens);
            response.put("devices", deviceTokenDetails);
            response.put("server-details", serverDetails);

            return response;

        }catch (Exception e){
            response.put("global-tokens", 0);
            response.put("devices", 0);
            response.put("server-details", 0);
            response.put("error","Unexpected Error at ManagementService");

            System.out.println(e.getMessage());

            return response;
        }
    }
}
