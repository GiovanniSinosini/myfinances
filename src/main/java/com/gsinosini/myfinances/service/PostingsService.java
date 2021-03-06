package com.gsinosini.myfinances.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.StatusPostings;

public interface PostingsService {
	
	Postings save (Postings posting);
	
	Postings update (Postings posting);

	void delete (Postings posting);
	
	List<Postings> search (Postings filterPostings);
	
	void updateStatus (Postings posting, StatusPostings status);
	
	void validate (Postings posting);
	
	Optional<Postings> getId(Long id);
	
	BigDecimal getBalanceByUser(Long id);

}
