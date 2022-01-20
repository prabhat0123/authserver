package com.abc.authservice;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.abc.authservice.domain.Role;
import com.abc.authservice.domain.User;
import com.abc.authservice.service.UserService;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	CommandLineRunner run(UserService authService) {
		
		return args -> {

			authService.saveRole(new Role(null,"ROLE_USER"));
			authService.saveRole(new Role(null,"ROLE_ADMIN"));
			authService.saveRole(new Role(null,"ROLE_BIZ_ADMIN"));
			

			authService.saveUser(new User(null,"PRABHAT KUMAR", "Peekay", "1234" , new ArrayList<>()));
			authService.saveUser(new User(null,"ABC USER 1", "USER_1", "1234" , new ArrayList<>()));
			authService.saveUser(new User(null,"ABC USER 2", "USER_2", "1234" , new ArrayList<>()));
			
			authService.addRoleToUser("Peekay","ROLE_USER");
			authService.addRoleToUser("USER_1","ROLE_ADMIN");
			authService.addRoleToUser("USER_2","ROLE_BIZ_ADMIN");
		};
		
	}
}
