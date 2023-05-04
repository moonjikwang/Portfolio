package com.Portfolio.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	private final UserService userService; 
	
	//회원가입
	@PostMapping("register")
	public String register(MemberDTO dto) {
		userService.register(dto);
		return "redirect:index";
	}
	
	//로그인
	@PostMapping("login")
	public String login(MemberDTO dto) {
		userService.login(dto);
		return "redirect:index";
	}
	
	@GetMapping("sessionOut")
	public String sessionOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:index";
	}
	
}
