package com.nineleaps.journalApp.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    @Value("${token.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    private void init(){
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKey(){
        return key;
    }


    // get username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T>T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any info from token we need the secret key
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    // check if token has expired
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generating token for user
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    // while creating the token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
