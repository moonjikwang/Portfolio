package com.Portfolio.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Data
public class Member extends BaseEntity{

	@Id
	private String email;
	private String showEmail; // 포트폴리오 노출용 이메일 카카오 이메일수집제한으로인해 별도생성
	private String name;
	private String password;
	private String profileImg;
	private String tel;
	private String gitUrl;
	private String skills;
	private String intro;
	private boolean state;
	
}
