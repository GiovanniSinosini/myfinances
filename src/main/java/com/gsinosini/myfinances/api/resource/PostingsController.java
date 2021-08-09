package com.gsinosini.myfinances.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gsinosini.myfinances.api.dto.PostingsDTO;
import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.entity.User;
import com.gsinosini.myfinances.model.enums.StatusPostings;
import com.gsinosini.myfinances.model.enums.TypePostings;
import com.gsinosini.myfinances.service.PostingsService;
import com.gsinosini.myfinances.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/postings")
@RequiredArgsConstructor
public class PostingsController {
	
	private final PostingsService postingsService;
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity save(@RequestBody PostingsDTO postingDTO) {
		
		try {
			Postings entity = converter (postingDTO);
			entity = postingsService.save(entity);
			return new ResponseEntity (entity, HttpStatus.CREATED);
			
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping ("{id}")
	public ResponseEntity update( @PathVariable ("id") Long id, @RequestBody PostingsDTO postingDTO) {
		return postingsService.getId(id).map( entity -> { 
			try {
				Postings posting = converter (postingDTO);
				posting.setId(entity.getId());
				postingsService.update(posting);
				return ResponseEntity.ok(posting);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity ("Posting not found in database.", HttpStatus.BAD_REQUEST));
	}
		
	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable ("id") Long id) {
		return postingsService.getId(id).map( entity -> {
			postingsService.delete(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity ("Posting not found in database.", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity find(
				@RequestParam(value = "description", required = false) String description,
				@RequestParam(value = "month", required = false) Integer month,
				@RequestParam(value = "year", required = false) Integer year,
				@RequestParam("user") Long idUser
			) {
		Postings filterPosting = new Postings();
		filterPosting.setDescription(description);
		filterPosting.setMonth(month);
		filterPosting.setYear(year);
		
		Optional<User> user = userService.searchById(idUser);
		if(user.isPresent()) {
			return ResponseEntity.badRequest().body("User not found.");
		} else {
			filterPosting.setUser(user.get());
		}
		
		List<Postings> posting = postingsService.search(filterPosting);
		return ResponseEntity.ok(posting);
		
	}
	
	private Postings converter(PostingsDTO postingDTO) {
		Postings posting = new Postings();
		posting.setId(postingDTO.getId());
		posting.setDescription(postingDTO.getDescription());
		posting.setYear(postingDTO.getYear());
		posting.setMonth(postingDTO.getMonth());
		posting.setValue(postingDTO.getValue());
		
		User user = userService
			.searchById(postingDTO.getId())
			.orElseThrow(() -> new BusinessRuleException("User not found."));
		
		posting.setUser(user);
		posting.setType(TypePostings.valueOf(postingDTO.getType()));
		posting.setStatus(StatusPostings.valueOf(postingDTO.getStatus()));
		
		return posting;
	}
	
}
