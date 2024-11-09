package com.nineleaps.journalApp.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;

    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // public urls
        String requestURI = request.getRequestURI();
        if ("/api/auth/**".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // get token
        String requestHeader = request.getHeader("Authorization");

        // Authorization = Bearer efveeve

        log.info("Header : {}", requestHeader);

        String name = null;
        String token = null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer ")){
            // looks good
            token = requestHeader.substring(7);

            try {
                name = jwtTokenHelper.getUsernameFromToken(token);
            }
            catch (IllegalArgumentException e){
                logger.info("Illegal Argument while fetching username !");
                e.printStackTrace();
            }
            catch (ExpiredJwtException e){
                logger.info("Given jwt Token has expired");
                e.printStackTrace();
            }
            catch (MalformedJwtException e){
                logger.info("Invalid Jwt token");
                e.printStackTrace();
            }
            catch (Exception e){
                logger.error("Error occurred ",e);
                e.printStackTrace();
            }
        }
        else {
            logger.info("Invalid Header Value !");
        }

        // now validate token
        if(name!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            Boolean validateToken = jwtTokenHelper.validateToken(token,userDetails);

            if(Boolean.TRUE.equals(validateToken)){
                // everything is alright and we have to authenticate
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else {
                logger.info("Validation for Jwt token has failed !");
            }
        }
        else{
            logger.info("Username is null or context is not null !");
        }

        filterChain.doFilter(request,response);
    }
}
