package com.gsinosini.myfinances.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsinosini.myfinances.model.entity.Postings;

public interface PostingsRepository extends JpaRepository<Postings, Long> {

}
