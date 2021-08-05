package com.gsinosini.myfinances.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.model.repository.UserRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void ValidateEmail() {
		Assertions.assertDoesNotThrow(() -> {
		//context
		userRepository.deleteAll();
		
		//action
		userService.emailValidation("email@email.com");
		
		});
	}
	
	@Test
	public void throwErrorWhenValidatingEmailExisting() {
		Assertions.assertThrows(BusinessRuleException.class, () -> {
		
		//context
		User user = User.builder().name("userTest").email("emailTest@email.com").build();
		userRepository.save(user);
		
		//action
		userService.emailValidation("emailTest@email.com");
		
		});
	}
}
