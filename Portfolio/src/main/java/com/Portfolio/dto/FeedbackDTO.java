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
public class FeedbackDTO {

	private Long no;
	private String email;
	private String content;	 //댓글내용
	private String replyerEmail;  //댓글작성자
	private String replyerName;
	private LocalDateTime regDate,modDate;
}
