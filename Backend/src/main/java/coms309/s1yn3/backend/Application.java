package coms309.s1yn3.backend;

import coms309.s1yn3.backend.entity.repository.PasswordRepository;
import coms309.s1yn3.backend.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	@Autowired
	UserRepository users;
	@Autowired
	PasswordRepository passwords;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
