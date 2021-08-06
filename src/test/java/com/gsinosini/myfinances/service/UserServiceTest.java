package com.gsinosini.myfinances.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.model.repository.UserRepository;
import com.gsinosini.myfinances.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		userService = new UserServiceImpl(userRepository);
	}
	
	@Test
	public void ValidateEmail() {
		Assertions.assertDoesNotThrow(() -> {
			
		//context
		Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//action
		userService.emailValidation("email@email.com");
		
		});
	}
	
	@Test
	public void throwErrorWhenValidatingEmailExisting() {
		Assertions.assertThrows(BusinessRuleException.class, () -> {
		
		//context
		Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//action
		userService.emailValidation("emailTest@email.com");
		
		});
	}
}
