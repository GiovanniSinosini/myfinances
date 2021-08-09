package com.gsinosini.myfinances.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostingsDTO {
	
	private Long id;
	private String description;
	private Integer month;
	private Integer year;
	private Long user;
	private BigDecimal value;
	private String type;
	private String status;


}
