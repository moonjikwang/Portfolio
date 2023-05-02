package com.Portfolio.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.dto.ResponseDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.service.MemberService;
import com.Portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;
	
	//회원가입 정의
	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public ResponseEntity<?> register(@RequestBody MemberDTO memberDTO, HttpServletRequest request,RedirectAttributes redirectAttributes) throws Throwable {

		try {
			//이메일 검증하여 기존회원여부 확인
			MemberDTO member = userService.findByEmail(null);
			HttpSession session = request.getSession();
			if(member != null) {
				session.setAttribute("", member);
			}
			
			Member entity = Member.builder()
					.email(memberDTO.getEmail())
					.name(memberDTO.getName())
					.password(memberDTO.getPassword())
					.build();
			
			Member registerMember = userService.create(entity);
			
			MemberDTO responseDTO = MemberDTO.builder()
					.email(registerMember.getEmail())
					.name(registerMember.getName())
					.password(registerMember.getPassword())
					.build();
			
			return ResponseEntity.ok().body(responseDTO);
			
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	//로그인 정의
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO) {
		Member member = userService.getBycredential(
				memberDTO.getEmail(), memberDTO.getPassword());
		
		if(member != null) {
			final MemberDTO responseDTO = MemberDTO.builder()
					.email(member.getEmail())
					.build();
			return ResponseEntity.ok().body(memberDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("로그인 실패")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
