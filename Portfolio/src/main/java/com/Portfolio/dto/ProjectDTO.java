package com.Portfolio.dto;


import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

	private String title;
	private String date;
	private String introduce;
	private String skill;
	@Column(length = 500)
	private String description;
	private String webLink;
	private String gitLink;
	private String imgUrl;
	private String email;
	
}
