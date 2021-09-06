package com.gsinosini.myfinances.service;

import com.gsinosini.myfinances.model.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	
	String generateToken(User user);
	
	Claims getClaims(String token) throws ExpiredJwtException;
	
	boolean isTokenValid(String token);
	
	String getLoginUser(String token);
}