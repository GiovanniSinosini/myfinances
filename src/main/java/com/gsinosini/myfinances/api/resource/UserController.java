package com.gsinosini.myfinances.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsinosini.myfinances.api.dto.UserDTO;
import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.exception.ErrorAuthentication;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity SaveUser( @RequestBody UserDTO userDto) {
		
		User user = User.builder()
						.name(userDto.getName())
						.email(userDto.getEmail())
						.password(userDto.getPassword()).build();
		try {
			User userSalve = userService.userSave(user);
			return new ResponseEntity(userSalve, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/authentication")
	public ResponseEntity UserAuthentication( @RequestBody UserDTO userDto) {

		try {
			User authenticatedUser = userService.authentication(userDto.getEmail(), userDto.getPassword());
			return ResponseEntity.ok().body(authenticatedUser);
		
		} catch (ErrorAuthentication e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}
