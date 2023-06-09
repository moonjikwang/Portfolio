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
import lombok.ToString;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Comments extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cno;		 //댓글넘버
	private String text;	 //댓글내용
	private String replyer;  //댓글작성자
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;
	@ManyToOne(fetch = FetchType.LAZY)
	private Member writer;
}