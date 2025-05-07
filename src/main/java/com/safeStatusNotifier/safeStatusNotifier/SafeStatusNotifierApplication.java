package com.safeStatusNotifier.safeStatusNotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.safeStatusNotifier.safeStatusNotifier.repositories")
@EntityScan(basePackages = "com.safeStatusNotifier.safeStatusNotifier.entity")

public class SafeStatusNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeStatusNotifierApplication.class, args);
	}

}
