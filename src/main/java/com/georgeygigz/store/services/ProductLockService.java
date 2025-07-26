package com.georgeygigz.store.services;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductLockService {

    private static final String PRODUCT_BLOCKER_PREFIX = "product_blocker:";
    private final RedisTemplate<String, Object> redisTemplate;

    public void blockProduct(Long productId) {
        redisTemplate.opsForValue().set(PRODUCT_BLOCKER_PREFIX + productId, "blocked");
    }

    public void unblockProduct(Long productId) {
        redisTemplate.delete(PRODUCT_BLOCKER_PREFIX + productId);
    }

    public boolean isBlocked(Long productId) {
        return redisTemplate.hasKey(PRODUCT_BLOCKER_PREFIX + productId);
    }
}
