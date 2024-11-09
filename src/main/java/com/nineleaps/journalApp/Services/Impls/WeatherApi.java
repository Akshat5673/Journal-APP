package com.nineleaps.journalApp.Services.Impls;


import com.nineleaps.journalApp.Cache.AppCache;
import com.nineleaps.journalApp.Dtos.WeatherResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.nineleaps.journalApp.Enums.Keys.WEATHER_API;
import static com.nineleaps.journalApp.Utils.Constants.API_KEY;
import static com.nineleaps.journalApp.Utils.Constants.CITY;

@Component
public class WeatherApi {

    private final RedisService redisService;
    private final RestTemplate restTemplate;
    private final AppCache appCache;  
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    public WeatherApi(RedisService redisService, RestTemplate restTemplate, AppCache appCache) {
        this.redisService = redisService;
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }


    public WeatherResponse getWeather(String city) throws IOException {
        WeatherResponse weatherResponse = redisService.get("weather of: " + city, WeatherResponse.class);
        if(!ObjectUtils.isEmpty(weatherResponse))
            return weatherResponse;
        else{
            String finalApi = appCache.getCache().get(WEATHER_API.toString()).replace(CITY,city).replace(API_KEY,apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate
                    .exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(!ObjectUtils.isEmpty(body))
                redisService.set("weather of: " + city,body,300L);
            return body;
        }



        //In case we need to send a post call then we do it like this:

        // sending header in key value pair -
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("key","value");

//        sending request body/entity - and put it in place of null below and replace get with post
//        User user = User.builder().name("Alex").password("Alex100").build();
//        HttpEntity<User> entity = new HttpEntity<>(user,headers);



    }
}
