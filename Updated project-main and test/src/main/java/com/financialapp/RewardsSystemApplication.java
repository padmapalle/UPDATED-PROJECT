package com.financialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RewardsSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardsSystemApplication.class, args);
	}

}
