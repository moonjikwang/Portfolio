package com.Portfolio.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.service.MemberService;
import com.Portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private final UserService userService; 
	@Autowired
	MemberService memberService;
	
	//회원가입
	@PostMapping("register")
	public String register(MemberDTO dto,HttpServletResponse res) {
		MemberDTO member = userService.register(dto);
		PrintWriter printWriter = null;
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");
		if (member == null) {
			try {
				printWriter = res.getWriter();
	            printWriter.println("<script type='text/javascript'>"
	                    + "alert('회원가입에 실패했습니다. 이미 있는 계정입니다.');"
	                    + "history.back();"
	                    +"</script>");

	            printWriter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
	                if(printWriter != null) { printWriter.close(); }
	            
	        }
		}
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
	
	@PostMapping("updateState")
	public String stateForm(MemberDTO dto) {
		memberService.setState(dto);
		return "redirect:myPage";
	}
	
}
