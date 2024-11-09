package com.nineleaps.journalApp.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nineleaps.journalApp.Cache.AppCache;
import com.nineleaps.journalApp.Dtos.ApiResponse;
import com.nineleaps.journalApp.Dtos.WeatherResponse;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Services.Impls.WeatherApi;
import com.nineleaps.journalApp.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN_USER')")
public class UserController {
    private final UserService userService;

    private final WeatherApi weatherApi;

    private final AppCache appCache;

    @Autowired
    public UserController(UserService userService, WeatherApi weatherApi, AppCache appCache) {
        this.userService = userService;
        this.weatherApi = weatherApi;
        this.appCache = appCache;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN_CREATE')")
    public ResponseEntity<User> createAdmin(@Valid @RequestBody User user){
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    public ResponseEntity<List<User>> listUser(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    public ResponseEntity<User> retrieveUser(@PathVariable String id){
        return ResponseEntity.ok(userService.retrieve(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id){
        userService.delete(id);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully!",true),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody User user){
            return ResponseEntity.ok(userService.update(id,user));
    }

    @GetMapping("/greet/city/{city}")
    public ResponseEntity<ApiResponse<WeatherResponse>> greetings(@PathVariable String city) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse response = weatherApi.getWeather(city);
        return ResponseEntity.ok(new ApiResponse<>(String.format("Hello %s, here is the weather information about %s",
                auth.getName(), city),response,true));
    }

    // to be used if value has been changed from db while the application is running and weather api throws 500,
    // so without re-running the app we can clear cache and hit the endpoint again
    @GetMapping("/clear-app-cache")
    public ResponseEntity<ApiResponse> clearAppCache(){
        appCache.init();
        return ResponseEntity.ok(new ApiResponse("Cache has been cleared",true));
    }

    @GetMapping("/getSAUsers")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    public ResponseEntity<List<User>> getUsersForSAnalysis(){
        return ResponseEntity.ok(userService.getUsersForAnalysis());
    }

    @GetMapping("/sendSentiment")
    public ResponseEntity<ApiResponse> sendSentimentMail(){
        userService.sendSentimentMail();
        return ResponseEntity.ok(new ApiResponse("Sentiment sent to kafka server ! ",true));
    }

}
