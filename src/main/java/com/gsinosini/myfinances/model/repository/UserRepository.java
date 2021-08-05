package com.gsinosini.myfinances.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsinosini.myfinances.model.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);
	
}
