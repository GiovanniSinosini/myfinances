package com.gsinosini.myfinances.service.impl;

import java.util.Optional;

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
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User authentication(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);
		
		if (!user.isPresent()) {
			throw new ErrorAuthentication("User not found.");
		} else if (!user.get().getPassword().equals(password)) {
			throw new ErrorAuthentication("Invalid password.");
		}
		return user.get();
	}

	@Override
	@Transactional
	public User userSave(User user) {
		emailValidation(user.getEmail());
		return userRepository.save(user);
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
