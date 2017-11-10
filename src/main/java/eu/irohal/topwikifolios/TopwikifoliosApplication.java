package eu.irohal.topwikifolios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TopwikifoliosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopwikifoliosApplication.class, args);
	}

}
