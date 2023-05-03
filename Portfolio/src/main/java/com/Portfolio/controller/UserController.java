package com.Portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private final UserService service; 
	
	//회원가입
	@PostMapping("register")
	public String register(MemberDTO dto) {
		service.register(dto);
		return "redirect:index";
	}
	
	//로그인
	@PostMapping("login")
	public String login() {
		
		return "redirect:index";
	}
}
