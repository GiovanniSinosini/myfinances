package com.gsinosini.myfinances.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.StatusPostings;
import com.gsinosini.myfinances.model.enums.TypePostings;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PostingsRepositoryTest {
	
	@Autowired
	PostingsRepository postingRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void saveAPostings() {
		//context
		Postings posting = createPosting();
		//action
		posting = postingRepository.save(posting);
		
		//verification
		assertThat(posting.getId()).isNotNull();
	}
	
	@Test
	public void deleteAPostings() {
		//context
		Postings posting = createAndPersistPosting();
		posting = entityManager.find(Postings.class, posting.getId());
		
		//action
		postingRepository.delete(posting);
		Postings postingNonexistent = entityManager.find(Postings.class, posting.getId());
		
		//verification
		assertThat(postingNonexistent).isNull();
	}

	
	
	public void updatePostings() {
		//context
		Postings posting = createAndPersistPosting();
		
		//action
		posting.setYear(2022);
		posting.setDescription("Test update");
		posting.setStatus(StatusPostings.CANCELED);

		postingRepository.save(posting);
		
		Postings postingUpdated = entityManager.find(Postings.class, posting.getId());
		
		//verification
		assertThat(postingUpdated.getYear()).isEqualTo(2022);
		assertThat(postingUpdated.getDescription()).isEqualTo("Test update");
		assertThat(postingUpdated.getStatus()).isEqualTo(StatusPostings.CANCELED);
	}
	
	@Test
	public void searchPostingById() {
		//context
		Postings posting = createAndPersistPosting();
		
		//action
		Optional<Postings> postingFound = postingRepository.findById(posting.getId());
		
		//verification
		assertThat(postingFound.isPresent()).isTrue();
		
	}
	
	private Postings createPosting() {
		return Postings.builder()
				.year(2021)
				.month(9)
				.description("Example posting")
				.value(BigDecimal.valueOf(10))
				.type(TypePostings.RECEIPTS)
				.status(StatusPostings.PENDING)
				.dateRegister(LocalDate.now())
				.build();
	}
	
	private Postings createAndPersistPosting() {
		Postings posting = createPosting();
		entityManager.persist(posting);
		return posting;
	}
}
