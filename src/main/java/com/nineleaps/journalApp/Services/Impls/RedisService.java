package com.nineleaps.journalApp.Services.Impls;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {


    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public <T>T get(String key, Class<T> weatherResponseClass) throws IOException {

            Object response = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            // to delete a k/v pair
            // redisTemplate.delete("weather");
            if(ObjectUtils.isEmpty(response))
                return null;
            else
                return mapper.readValue(response.toString(),weatherResponseClass);
    }

    public void set(String key, Object o, Long ttl){

        // if we set the value of ttl as -1 then it will never expire
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.SECONDS);
        }
        catch (Exception e){
            log.error("Exception ",e);
        }
    }

}
