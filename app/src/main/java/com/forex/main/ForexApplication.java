package com.forex.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EntityScan("com.forex.entity")
@EnableJpaRepositories("com.forex.repository")
@SpringBootApplication(scanBasePackages = "com.forex")
public class ForexApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}
}
