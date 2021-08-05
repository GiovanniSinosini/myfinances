package com.gsinosini.myfinances.service.impl;

import org.springframework.stereotype.Service;

import com.gsinosini.myfinances.exception.BusinessRuleException;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User userSave(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void emailValidation(String email) {
		boolean exists = userRepository.existsByEmail(email);
		if (exists) {
			throw new BusinessRuleException("There is already a registered user with this email.");
		}
	}
	
	
	
	
	
	
}
