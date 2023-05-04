package com.Portfolio.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

	private String email;
	private String password;
	private String name;
	private String nickName;
	private String profileImg;
	private String tel;
	private String gitUrl;
	private String showEmail;
	private String skills;
	private String intro;
	private LocalDateTime regDate,modDate;
	private String token;
	

}
