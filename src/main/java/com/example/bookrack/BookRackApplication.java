package com.example.bookrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BookRackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookRackApplication.class, args);
	}

}
