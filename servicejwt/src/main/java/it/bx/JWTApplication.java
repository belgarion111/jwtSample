package it.bx;

import it.bx.bean.FormRole;
import it.bx.bean.FormUser;
import it.bx.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class JWTApplication {

	public static void main(String[] args) {
		SpringApplication.run(JWTApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new FormRole(null , "ROLE_USER"));
			userService.saveRole(new FormRole(null , "ROLE_ADMIN"));

			userService.saveUser(new FormUser(null , "NAME USER ONE" , "USER1" , "PASSWORD" , new ArrayList()));
			userService.saveUser(new FormUser(null , "NAME USER TWO" , "USER2" , "PASSWORD" , new ArrayList()));
			userService.addRoleToUser("ROLE_ADMIN" , "USER2" );
			userService.addRoleToUser("ROLE_USER" , "USER1" );
		};
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
