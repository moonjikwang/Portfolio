package com.Portfolio.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.Portfolio.dto.MemberDTO;
import com.Portfolio.service.GithubService;
import com.Portfolio.service.MemberService;

@Controller
public class GithubController {

	@Autowired
	GithubService githubService;
	@Autowired
	MemberService memberService;
	
	@GetMapping("githubOauth")
	public String getUserInfo(@RequestParam String code,HttpServletRequest req) throws Throwable {
		HttpSession session = req.getSession();
		String accessToken = githubService.getAccessToken(code);
		MemberDTO userInfo = githubService.getUserInfo(accessToken);
		MemberDTO member = memberService.findByEmail(userInfo.getEmail());
		if(member != null) {
			session.setAttribute("userInfo",member);
		}else {
			MemberDTO newMember = memberService.register(userInfo);
			session.setAttribute("userInfo", newMember);
		}
	    return "redirect:index";
	}

	
}
