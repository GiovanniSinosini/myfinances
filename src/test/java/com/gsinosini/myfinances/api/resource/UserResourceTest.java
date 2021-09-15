package com.gsinosini.myfinances.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsinosini.myfinances.api.dto.UserDTO;
import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.exception.ErrorAuthentication;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.service.PostingsService;
import com.gsinosini.myfinances.service.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserResourceTest {

	static final String API = "/api/users";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;

	@MockBean
	UserService userService;
	
	@MockBean
	PostingsService postingService;

	@Test
	public void AuthUser() throws Exception {
		// context
		String email = "user@email.com";
		String password = "123";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		User userAuthenticated = User.builder().id(1l).email(email).password(password).build();
		Mockito.when( userService.authentication(email, password) ).thenReturn(userAuthenticated);
		String jsonContent = new ObjectMapper().writeValueAsString(userDTO);
		
		// action and verification
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post (API.concat("/authentication") )
													.accept(JSON)
													.contentType(JSON)
													.content(jsonContent);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(userAuthenticated.getId()) )
			.andExpect(MockMvcResultMatchers.jsonPath("name").value(userAuthenticated.getName()) )
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(userAuthenticated.getEmail()) );
	}
	
	@Test
	public void notAuthUserReturnBadRequest() throws Exception {
		// context
		String email = "user@email.com";
		String password = "123";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		Mockito.when( userService.authentication(email, password) ).thenThrow(ErrorAuthentication.class);
	
		String jsonContent = new ObjectMapper().writeValueAsString(userDTO);
		
		// action and verification
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post (API.concat("/authentication") )
													.accept(JSON)
													.contentType(JSON)
													.content(jsonContent);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest() );
	}
	
	@Test
	public void postUserTest() throws Exception {
		// context
		String email = "user@email.com";
		String password = "123";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		User user = User.builder().id(1l).email(email).password(password).build();
		
		Mockito.when( userService.userSave(Mockito.any(User.class)) ).thenReturn(user);
		String jsonContent = new ObjectMapper().writeValueAsString(userDTO);
		
		// action and verification
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post (API)
													.accept(JSON)
													.contentType(JSON)
													.content(jsonContent);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()) )
			.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()) )
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()) );
	}
	
	@Test
	public void notPostUserTestReturnBadRequest() throws Exception {
		// context
		String email = "user@email.com";
		String password = "123";
		
		UserDTO userDTO = UserDTO.builder().email(email).password(password).build();
		
		Mockito.when( userService.userSave(Mockito.any(User.class)) ).thenThrow(BusinessRuleException.class);
		String jsonContent = new ObjectMapper().writeValueAsString(userDTO);
		
		// action and verification
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post (API)
													.accept(JSON)
													.contentType(JSON)
													.content(jsonContent);
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest() );
	}
}
