package com.forexservice.forexservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.forexservice.forexservice.respositories")
public class ForexserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForexserviceApplication.class, args);
	}

}
