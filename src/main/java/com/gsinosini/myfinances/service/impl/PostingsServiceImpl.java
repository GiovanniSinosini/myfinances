package com.gsinosini.myfinances.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsinosini.myfinances.exception.BusinessRuleException;
import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.StatusPostings;
import com.gsinosini.myfinances.model.enums.TypePostings;
import com.gsinosini.myfinances.model.repository.PostingsRepository;
import com.gsinosini.myfinances.service.PostingsService;

@Service
public class PostingsServiceImpl implements PostingsService{

	private PostingsRepository postingRepository;
	
	public PostingsServiceImpl (PostingsRepository postingRepository) {
		this.postingRepository = postingRepository;
	}
	
	@Override
	@Transactional
	public Postings save(Postings posting) {
		validate(posting);
		posting.setStatus(StatusPostings.PENDING);
		return postingRepository.save(posting);
	}

	@Override
	@Transactional
	public Postings update(Postings posting) {
		Objects.requireNonNull(posting.getId());
		validate(posting);
		return postingRepository.save(posting);
	}

	@Override
	@Transactional
	public void delete(Postings posting) {
		Objects.requireNonNull(posting.getId());
		postingRepository.delete(posting);
	}

	@Override
	@Transactional (readOnly = true)
	public List<Postings> search(Postings filterPosting) {
		Example example = Example.of(filterPosting, 
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		return postingRepository.findAll(example);
	}

	@Override
	public void updateStatus(Postings posting, StatusPostings status) {
		posting.setStatus(status);
		update(posting);
	}

	@Override
	public void validate(Postings posting) {
		
		if (posting.getDescription() == null || posting.getDescription().trim().equals("")) {
			throw new BusinessRuleException("Enter a valid description.");
		}
		
		if (posting.getMonth() == null || posting.getMonth() < 1 || posting.getMonth() > 12) {
			throw new BusinessRuleException("Enter a valid month.");
		}
		
		if (posting.getYear() == null || posting.getYear().toString().length() !=4 ){
			throw new BusinessRuleException("Enter a valid year.");
		}
		
		if (posting.getUser() == null || posting.getUser().getId() == null ){
			throw new BusinessRuleException("Enter a valid user.");
		}
		
		if (posting.getValue() == null || posting.getValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new BusinessRuleException("Enter a valid value.");
		}
		
		if (posting.getType() == null ){
			throw new BusinessRuleException("Enter a valid type.");
		}
	}

	@Override
	public Optional<Postings> getId(Long id) {
		return postingRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getBalanceByUser(Long id) {
		BigDecimal receipts = postingRepository.getBalanceByTypePosting(id, TypePostings.RECEIPTS);
		BigDecimal payments = postingRepository.getBalanceByTypePosting(id, TypePostings.PAYMENTS);
		
		if (receipts == null) {
			receipts = BigDecimal.ZERO;
		}
		if (payments == null) {
			payments = BigDecimal.ZERO;
		}
		
		return receipts.subtract(payments);
	}
}
