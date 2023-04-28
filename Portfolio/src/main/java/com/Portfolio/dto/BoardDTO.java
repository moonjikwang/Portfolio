package com.Portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

	private Long bno;
	private String title;
	private String content;
	private String email;
	private String name;
	private int viewCount;
	private int commentsCount;
	private LocalDateTime regDate,modDate;
}
