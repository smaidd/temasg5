package com.example.lab9Demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Lab9DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab9DemoApplication.class, args);
	}

}
