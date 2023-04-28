package com.Portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentsDTO {
	
	private Long cno;       //댓글 번호
	private String text;    //내용
	private String replyer; //댓글작성
	private Long bno;       //게시물 번호
	private String email;
	private String profileImg;
	private LocalDateTime regDate, modDate; //등록일, 수정일
}
