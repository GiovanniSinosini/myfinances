package com.gsinosini.myfinances.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.exception.ErrorAuthentication;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.model.repository.UserRepository;
import com.gsinosini.myfinances.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
		super();
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@Override
	public User authentication(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);
		
		if (!user.isPresent()) {
			throw new ErrorAuthentication("User not found.");
		}  
		
		boolean passwordMatch = encoder.matches(password, user.get().getPassword());
		
		if (!passwordMatch) {
			throw new ErrorAuthentication("Invalid password.");
		}
		return user.get();
	}

	@Override
	@Transactional
	public User userSave(User user) {
		emailValidation(user.getEmail());
		encoderPassword(user);
		return userRepository.save(user);
	}

	private void encoderPassword(User user) {
		String password = user.getPassword();
		String passwordCoded = encoder.encode(password);
		user.setPassword(passwordCoded);
	}

	@Override
	public void emailValidation(String email) {
		boolean exists = userRepository.existsByEmail(email);
		if (exists) {
			throw new BusinessRuleException("There is already a registered user with this email.");
		}
	}

	@Override
	public Optional<User> searchById(Long id) {
		return userRepository.findById(id);
	}
	
}
