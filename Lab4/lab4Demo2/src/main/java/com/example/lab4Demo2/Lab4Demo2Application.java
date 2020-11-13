package com.example.lab4Demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@SpringBootApplication
public class Lab4Demo2Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab4Demo2Application.class, args);
	}

	@Bean
	@ApplicationScope
	public Counter applicationCounter(){
		return new Counter();
	}

	@Bean
	@SessionScope
	public Counter sessionCounter(){
		return new Counter();
	}

	@Bean
	@RequestScope
	public Counter requestCounter(){
		return new Counter();
	}
}
