package com.Portfolio.dto;

import java.io.Serializable;

import com.Portfolio.entity.Member;

//세션 정보 저장 DTO
public class UserSessionDTO implements Serializable{

	private String email;
	private String password;
	private String name;
	private String nickName;
	
	//entity-> DTO
	public UserSessionDTO(Member member) {
		this.email = member.getEmail();
		this.password = member.getPassword();
		this.name = member.getPassword();
		this.nickName = member.getNickName();
	}
}
