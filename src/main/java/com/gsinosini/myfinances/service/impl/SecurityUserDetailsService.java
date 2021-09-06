package com.gsinosini.myfinances.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.model.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	
	public SecurityUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User userFound = userRepository
				.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Unregistered email"));
			
		return org.springframework.security.core.userdetails
					.User.builder()
					.username(userFound.getEmail())
					.password(userFound.getPassword())
					.roles("USER")
					.build();
	}
}
