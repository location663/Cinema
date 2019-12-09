package com.stylefeng.guns.rest.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class CacheService {
    private Cache<String, Object> cache;

    @PostConstruct
    public void init(){
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100L)
                .build();
    }

    public void put(String key, Object val){
        cache.put(key, val);
    }

    public Object get(String key){
        Object val = cache.getIfPresent(key);
        return val;
    }
}
