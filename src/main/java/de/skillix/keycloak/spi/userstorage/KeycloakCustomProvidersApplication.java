package de.skillix.keycloak.spi.userstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeycloakCustomProvidersApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakCustomProvidersApplication.class, args);
	}

}
