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

	//이메일: 계정@도메인.최상위도메인
	@NotBlank(message = "이메일은 필수 입력 값 입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", 
			 message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	//비밀번호: 최소 8자 및 최대 10자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자
	@NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$")
	private String password;
	
	//이름
	@NotBlank(message = "이름은 필수 입력 값 입니다.")
	private String name;
	
	//닉네임: 2자 이상 8자 이하, 영어 또는 숫자 또는 한글
	@Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,8}$",
			message = "닉네임은 2~8자리여야 하며 한글 초성 및 모음은 허용하지 않습니다.")
	private String nickName;
	
	private String profileImg;
	private String tel;
	private String gitUrl;
	private String showEmail;
	private String skills;
	private String intro;
	private LocalDateTime regDate,modDate;
	

}
