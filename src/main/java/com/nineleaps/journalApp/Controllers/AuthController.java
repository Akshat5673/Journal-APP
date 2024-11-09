package com.nineleaps.journalApp.Controllers;


import com.nineleaps.journalApp.Cache.AppCache;
import com.nineleaps.journalApp.Dtos.ApiResponse;
import com.nineleaps.journalApp.Dtos.JwtAuthRequest;
import com.nineleaps.journalApp.Dtos.JwtAuthResponse;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Security.JwtTokenHelper;
import com.nineleaps.journalApp.Services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;



    @Autowired
    public AuthController(JwtTokenHelper jwtTokenHelper, UserDetailsService userDetailsService
            , AuthenticationManager authenticationManager, UserService userService) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request){
        authenticate(request.getName(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getName());
        String token = jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = JwtAuthResponse.builder()
                .token(token)
                .name(userDetails.getUsername())
                .build();

        return ResponseEntity.ok(response);
    }

    private void authenticate(String name, String password) {

        logger.info("Authenticating user with name: {}", name);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(name,password);

        try {
            authenticationManager.authenticate(authenticationToken);
            logger.info("Authentication successful for user: {}", name);
        }
        catch (BadCredentialsException e){
            logger.error("Authentication failed for user: {}", name);
            throw new BadCredentialsException("Invalid Username or password !");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerNewUser(@Valid @RequestBody User user){
        userService.saveNewUser(user);
        return new ResponseEntity<>(new ApiResponse("User Registered Successfully",true)
                , HttpStatus.CREATED);
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

}
