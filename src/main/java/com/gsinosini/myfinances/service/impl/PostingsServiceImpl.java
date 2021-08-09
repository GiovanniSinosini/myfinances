package com.gsinosini.myfinances.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.StatusPostings;
import com.gsinosini.myfinances.model.repository.PostingsRepository;
import com.gsinosini.myfinances.service.PostingsService;

@Service
public class PostingsServiceImpl implements PostingsService{

	private PostingsRepository postingsRepository;
	
	public PostingsServiceImpl (PostingsRepository postingRepository) {
		this.postingsRepository = postingsRepository;
	}
	
	@Override
	public Postings save(Postings posting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Postings update(Postings posting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Postings posting) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Postings> search(Postings filterPostings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(Postings posting, StatusPostings status) {
		// TODO Auto-generated method stub
		
	}

}
