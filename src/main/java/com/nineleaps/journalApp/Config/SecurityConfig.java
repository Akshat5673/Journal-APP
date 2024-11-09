package com.nineleaps.journalApp.Config;

import com.nineleaps.journalApp.Security.CustomUserDetailsService;
import com.nineleaps.journalApp.Security.JwtAuthenticationEntryPoint;
import com.nineleaps.journalApp.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"

    };

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //configuration

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(PUBLIC_URLS).permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
