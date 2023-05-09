package com.Portfolio.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioDTO {

	private String email;
	private String name;
	private String profileImg;
	private String tel;
	private String gitUrl;
	private String showEmail;
	private String skills;
	private String intro;
	private int viewCount;
	private int replyCount;
	private List<ProjectDTO> projects;
	private LocalDateTime regDate,modDate;
}
