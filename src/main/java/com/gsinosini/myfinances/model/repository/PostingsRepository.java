package com.gsinosini.myfinances.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gsinosini.myfinances.model.entity.Postings;
import com.gsinosini.myfinances.model.enums.TypePostings;

public interface PostingsRepository extends JpaRepository<Postings, Long> {

	@Query (value=
			" select sum(p.value) from Postings p join p.user u "
			+ " where u.id = :idUser and p.type = :type group by u ")
	BigDecimal getBalanceByTypePosting(@Param("idUser") Long idUser, @Param("type") TypePostings type);
}
