package com.Portfolio.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Feedback extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;		 //댓글넘버
	private String content;	 //댓글내용
	private String replyerEmail;  //댓글작성자
	private String replyerName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member writer;
}