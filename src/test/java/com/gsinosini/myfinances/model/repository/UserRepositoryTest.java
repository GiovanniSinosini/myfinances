package com.gsinosini.myfinances.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.model.entity.User;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void CheckforEmail() {
		
		//context
		User user = User.builder().name("userTest").email("userTest@email.com").build();
		userRepository.save(user);
		
		// action
		boolean result = userRepository.existsByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void returnsFalseWhenThereIsNoUserWithEmail() {
		//context
		userRepository.deleteAll();
		
		//action
		boolean result = userRepository.existsByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result).isFalse();
		
	}
	
}
