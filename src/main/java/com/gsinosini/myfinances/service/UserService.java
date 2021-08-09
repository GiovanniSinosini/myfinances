package com.gsinosini.myfinances.service;

import java.util.Optional;

import com.gsinosini.myfinances.model.entity.User;

public interface UserService {
	
	User authentication (String email, String password);
	
	User userSave (User user);
	
	void emailValidation (String email);
	
	Optional<User> searchById(Long id);

}
