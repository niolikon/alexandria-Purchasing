package org.niolikon.alexandria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AlexandriaPurchasingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlexandriaPurchasingApplication.class, args);
	}

}
