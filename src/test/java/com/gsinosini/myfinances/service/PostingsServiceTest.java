package com.gsinosini.myfinances.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.StatusPostings;
import com.gsinosini.myfinances.model.repository.PostingsRepository;
import com.gsinosini.myfinances.model.repository.PostingsRepositoryTest;
import com.gsinosini.myfinances.service.impl.PostingsServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PostingsServiceTest {
	
	@SpyBean
	PostingsServiceImpl postingService;
	
	@MockBean
	PostingsRepository postingRepository;
	
	@Test
	public void savePosting() {
		//context
		Postings postingForSave = PostingsRepositoryTest.createPosting();
		Mockito.doNothing().when(postingService).validate(postingForSave);
		
		Postings postingSaved = PostingsRepositoryTest.createPosting();
		postingSaved.setId(1l);
		postingSaved.setStatus(StatusPostings.PENDING);
		Mockito.when(postingRepository.save(postingForSave)).thenReturn(postingSaved);
		
		//action
		Postings posting = postingService.save(postingForSave);
		
		//verification
		Assertions.assertThat(posting.getId()).isEqualTo(postingSaved.getId());
		Assertions.assertThat(posting.getStatus()).isEqualTo(StatusPostings.PENDING);
	}
	
	@Test
	public void notSavePostingWithErrorValidation() {
		//context
		Postings postingForSave = PostingsRepositoryTest.createPosting();
		Mockito.doThrow(BusinessRuleException.class).when(postingService).validate(postingForSave);
				
		//action and verification
		Assertions.catchThrowableOfType(() -> postingService.save(postingForSave), BusinessRuleException.class);
		Mockito.verify(postingRepository, Mockito.never()).save(postingForSave);
	}
	
	@Test
	public void updatePosting() {
		//context
		Postings postingSave = PostingsRepositoryTest.createPosting();
		postingSave.setId(1l);
		postingSave.setStatus(StatusPostings.PENDING);
		
		Mockito.doNothing().when(postingService).validate(postingSave);
		
		Mockito.when(postingRepository.save(postingSave)).thenReturn(postingSave);
		
		//action
		postingService.save(postingSave);
		
		//verification
		Mockito.verify(postingRepository, Mockito.times(1)).save(postingSave);
	}
	
	@Test
	public void throwErrorUpdateForPostingNotSave() {
		//context
		Postings postingForSave = PostingsRepositoryTest.createPosting();
				
		//action
		Assertions.catchThrowableOfType(() -> postingService.update(postingForSave), NullPointerException.class);
		Mockito.verify(postingRepository, Mockito.never()).save(postingForSave);
	}
	
	@Test
	public void deletePosting() {
		//context
		Postings posting = PostingsRepositoryTest.createPosting();
		posting.setId(1l);
				
		//action
		postingService.delete(posting);

		//verification
		Mockito.verify(postingRepository).delete(posting);
	}
	
	@Test
	public void throwErrorDeleteForPostingWithoutId() {
		//context
		Postings posting = PostingsRepositoryTest.createPosting();
				
		//action
		Assertions.catchThrowableOfType(() -> postingService.delete(posting), NullPointerException.class);

		//verification
		Mockito.verify(postingRepository, Mockito.never()).delete(posting);
	}
	
	@Test
	public void filterPosting() {
		//context
		Postings posting = PostingsRepositoryTest.createPosting();
		posting.setId(1l);
		
		List<Postings> list = Arrays.asList(posting);
		Mockito.when(postingRepository.findAll(Mockito.any(Example.class))).thenReturn(list);
		
		//action
		List<Postings> result = postingService.search(posting);
		
		//verification
		Assertions
			.assertThat(result)
			.isNotEmpty()
			.hasSize(1)
			.contains(posting);
	}
	
	@Test
	public void updateStatusPosting() {
		//context
		Postings posting = PostingsRepositoryTest.createPosting();
		posting.setId(1l);
		posting.setStatus(StatusPostings.PENDING);
		
		StatusPostings newStatus = StatusPostings.EFFECTIVE;
		Mockito.doReturn(posting).when(postingService).update(posting);
		
		//action
		postingService.updateStatus(posting, newStatus);
		
		//verification
		Assertions.assertThat(posting.getStatus()).isEqualTo(newStatus);
		Mockito.verify(postingService).update(posting);
	}
	
	@Test
	public void getPostingById() {
		//context
		Long id = 1l;
		
		Postings posting = PostingsRepositoryTest.createPosting();
		posting.setId(id);
		
		Mockito.when(postingRepository.findById(id)).thenReturn(Optional.of(posting));
		
		//action
		Optional<Postings> result = postingService.getId(id);
		
		//verification
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void throwErrorNotFoundGetPostingById() {
		//context
		Long id = 1l;
		
		Postings posting = PostingsRepositoryTest.createPosting();
		posting.setId(id);
		
		Mockito.when(postingRepository.findById(id)).thenReturn(Optional.empty());
		
		//action
		Optional<Postings> result = postingService.getId(id);
		
		//verification
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	
	
}
