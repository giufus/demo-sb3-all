package com.giufus.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoSb3AllApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSb3AllApplication.class, args);
	}

}
