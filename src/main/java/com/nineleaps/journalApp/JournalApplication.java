package com.nineleaps.journalApp;

import com.nineleaps.journalApp.Services.Impls.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableScheduling
public class JournalApplication {


    public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
