package com.Portfolio.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.web.multipart.MultipartFile;

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
@ToString
public class Feedback extends BaseEntity{
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long feedbackNo;
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="writer_email")
	private Member writer;
	
	@ManyToOne
	@JoinColumn(name="receiver_email")
	private Member receiver;
	
	public void changeComments(String newComments) {
		this.comments = newComments;
	}
	
}
