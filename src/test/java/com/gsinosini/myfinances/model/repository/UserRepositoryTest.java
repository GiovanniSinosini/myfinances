package com.gsinosini.myfinances.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.model.entity.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TestEntityManager entityManagerTest;
	
	
	@Test
	public void CheckforEmail() {
		
		//context
		User user = User.builder().name("userTest").email("userTest@email.com").build();
		entityManagerTest.persist(user);
		
		// action
		boolean result = userRepository.existsByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void returnsFalseWhenThereIsNoUserWithEmail() {
		//context
		
		//action
		boolean result = userRepository.existsByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void thereMustBeUserInDatabase() {
		//context
		User user = createUser();
		
		//action
		User userExists = userRepository.save(user);
		
		// verification
		Assertions.assertThat(userExists.getId()).isNotNull();
	}
	
	@Test
	public void SearchUserByEmail() {
		//context
		User user = createUser();
		entityManagerTest.persist(user);
		
		//action
		Optional<User> result = userRepository.findByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result.isPresent()).isTrue();
}
	
	@Test
	public void SearchUserByEmailReturnEmptyWhereNonexistent() {
		//context
		
		//action
		Optional<User> result = userRepository.findByEmail("userTest@email.com");
		
		// verification
		Assertions.assertThat(result.isPresent()).isFalse();
}
	
	public static User createUser() {
		return  User
				.builder()
				.name("userTest")
				.email("userTest@email.com")
				.password("password")
				.build();
	}
}
