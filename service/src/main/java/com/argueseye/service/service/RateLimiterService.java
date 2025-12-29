package com.argueseye.service.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Component
public class RateLimiterService {

    private final Bucket globalBucket;
    private final LoadingCache<String,Bucket> bucketCache;

    private RateLimiterService(){

        Instant nextMidnightUtc = ZonedDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.DAYS)
                .plusDays(1)
                .toInstant();

        //pre day limit
        Bandwidth globalDailyLimit = Bandwidth.classic(5000,
                Refill.intervallyAligned(5000,Duration.ofDays(1),nextMidnightUtc,true));

        //this is the global token bucket
        globalBucket = Bucket.builder()
                .addLimit(globalDailyLimit)
                .build();

        //this is the bucket for each user
        Bandwidth devicePreMinuteLimit = Bandwidth.classic(5,Refill.greedy(5,Duration.ofMinutes(5)));
        Bandwidth devicePreDayLimit = Bandwidth.classic(30,Refill.greedy(30,Duration.ofDays(1)));

        bucketCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofDays(1))
                .maximumSize(50_000)
                .build(deviceId ->
                        Bucket.builder()
                                .addLimit(devicePreMinuteLimit)
                                .addLimit(devicePreDayLimit)
                                .build()
                );

    }

    public long getGlobalTokens(){
       return globalBucket.getAvailableTokens();
    }

    public List<String> bucketList(){
        List<String> myList = new ArrayList<>();

          bucketCache.asMap().forEach((deviceId,bucket)->{
              myList.add(deviceId);
          });

        return myList;
    }

    public long getAvilableTokenes(String deviceId){
        if (deviceId == null || deviceId.isBlank()) {
            //  reject
            throw new IllegalArgumentException("Missing device ID");
        }

        Bucket  tempBucket = bucketCache.get(deviceId.trim());
        return tempBucket.getAvailableTokens();
    }

    public boolean allowRequest(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            //  reject
            throw new IllegalArgumentException("Missing device ID");
        }

        Bucket tempBucket = bucketCache.get(deviceId.trim());

        if(globalBucket.tryConsume(1)&& tempBucket.tryConsume(1)){
            return true;
        }

        return false;
    }
}
