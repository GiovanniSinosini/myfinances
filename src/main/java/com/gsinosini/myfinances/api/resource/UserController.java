package com.gsinosini.myfinances.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsinosini.myfinances.api.dto.UserDTO;
import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.exception.ErrorAuthentication;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.service.PostingsService;
import com.gsinosini.myfinances.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final PostingsService postingService;
	
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
	
	@GetMapping("{id}/balance")
	public ResponseEntity getBalance(@PathVariable ("id") Long id) {
		
		Optional<User> user = userService.searchById(id);
		
		if (!user.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal balance = postingService.getBalanceByUser(id);
		return ResponseEntity.ok(balance);
	}
	
	
}
