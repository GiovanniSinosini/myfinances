package com.gsinosini.myfinances.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.exception.ErrorAuthentication;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.model.repository.UserRepository;
import com.gsinosini.myfinances.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	@SpyBean
	UserServiceImpl userService;
	
	@MockBean
	UserRepository userRepository;

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
	
	@Test
	public void UserAuthenticationWithSucess() {
		Assertions.assertDoesNotThrow(() -> {
		//context
		String email = "emailTest@test.com";
		String password = "password";
		
		User user = User.builder().email(email).password(password).build();
		Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
			
		//action
		User result = userService.authentication(email, password);
		
		// verification
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		});
	}
	@Test
	public void throwErrorUserAuthenticationWithThisEmail() {
		//context
		Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
		//action
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> userService.authentication("emailTest@test.com", "password") );
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class).hasMessage("User not found.");
	}
	
	@Test
	public void throwErrorUserAuthenticationWithThisPassword() {
		//context
		String password = "password";
		User userTest = User.builder().email("email@email.com").password(password).build();
		Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(userTest));
			
		//action
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> userService.authentication("emailTest@test.com", "123") );
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class).hasMessage("Invalid password.");
	}
	
	@Test
	public void SaveUser() {
		Assertions.assertDoesNotThrow(() -> {
		//context
		Mockito.doNothing().when(userService).emailValidation(Mockito.anyString());
		User userTest = User.builder()
				.id(1l)
				.name("name")
				.email("email@email.com")
				.password("password").build();
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userTest);
		
		//action
		User userSave = userService.userSave(new User());
		
		//Verification
		org.assertj.core.api.Assertions.assertThat(userSave).isNotNull();
		org.assertj.core.api.Assertions.assertThat(userSave.getId()).isEqualTo(1l);
		org.assertj.core.api.Assertions.assertThat(userSave.getName()).isEqualTo("name");
		org.assertj.core.api.Assertions.assertThat(userSave.getEmail()).isEqualTo("email@email.com");
		org.assertj.core.api.Assertions.assertThat(userSave.getPassword()).isEqualTo("password");
		});
	}
	
	@Test
	public void ErrorSaveUserExistsEmail() {
		Assertions.assertThrows(BusinessRuleException.class, () -> {
		//context
		String email = "email@email.com";
		User userTest = User.builder().email("email@email.com").build();
		Mockito.doThrow(BusinessRuleException.class).when(userService).emailValidation(email);
		
		//action
		userService.userSave(userTest);

		//Verification
		Mockito.verify(userRepository, Mockito.never()).save(userTest);
		
		});
	}
}
