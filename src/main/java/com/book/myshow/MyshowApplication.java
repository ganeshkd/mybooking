package com.book.myshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MyshowApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyshowApplication.class, args);
	}

}
