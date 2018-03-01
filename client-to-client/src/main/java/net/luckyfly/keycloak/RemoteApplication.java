package net.luckyfly.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class RemoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemoteApplication.class, args);
	}
}
