package com.Portfolio.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
	private final Errors errors;
	
	//회원가입
	@PostMapping("register")
	public String register(@Valid MemberDTO dto, Errors errors, Model model) {
		if (errors.hasErrors()) {
			//회원가입 실패시 입력 데이터 값을 유지
			model.addAttribute("memberDTO", dto);
			//유효성검사 통과하지 못한 필드와 메세지 핸들링
			Map<String, String> validatorResult = userService.validateHandling(errors);
			for (String key : validatorResult.keySet()) {
				model.addAttribute(key, validatorResult.get(key));
			}
			//회원가입 모달페이지로 리턴
			return "";
		}
		userService.register(dto);
		//검증이 끝나면 index페이지로 리다이렉트
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
	
	@GetMapping("myPage")
	public void myPage() {
		
	}
}
