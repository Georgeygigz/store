package com.georgeygigz.store.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Value("${spring.resilience4j.ratelimiter.configs.default.limitForPeriod}")
    private int limitForPeriod;

    @Value("${spring.resilience4j.ratelimiter.configs.default.limitRefreshPeriod}")
    private int limitRefreshPeriod;

    @Value("${spring.resilience4j.ratelimiter.configs.default.timeoutDuration}")
    private int timeoutDuration;

    @Bean
    public RateLimiterRegistry rateLimiterRegistry(){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(Duration.ofSeconds(limitRefreshPeriod))
                .timeoutDuration(Duration.ofMillis(timeoutDuration))
                .build();
        return RateLimiterRegistry.of(config);
    }
}
