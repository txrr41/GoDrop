package com.project.shiphub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShiphubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiphubApplication.class, args);
	}

}
