package com.gsinosini.myfinances.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtServiceImpl implements JwtService{
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	@Value("${jwt.signature-key}")
	private String signatureKey;

	@Override
	public String generateToken(User user) {
		long exp = Long.valueOf(expiration);
		LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault() ).toInstant();
		java.util.Date date = Date.from(instant);
		
		String hourExpirationToken = dateHourExpiration.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		String token = Jwts
							.builder()
							.setExpiration(date)
							.setSubject(user.getEmail())
							.claim("name", user.getName())
							.claim("hourExpiration", hourExpirationToken)
							.signWith(SignatureAlgorithm.HS512, signatureKey) 
							.compact();
		return token;
	}

	@Override
	public Claims getClaims(String token) throws ExpiredJwtException {
		return Jwts
				.parser()
				.setSigningKey(signatureKey)
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public boolean isTokenValid(String token) {
		try {
			Claims claims = getClaims(token);
			java.util.Date dateEx = claims.getExpiration();
			LocalDateTime dateExpiration = dateEx.toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDateTime();
			boolean dataHourValid = LocalDateTime.now().isAfter(dateExpiration);
			return !dataHourValid;
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String getLoginUser(String token) {
		Claims claims = getClaims(token);
		
		return claims.getSubject();
				
	}
}
