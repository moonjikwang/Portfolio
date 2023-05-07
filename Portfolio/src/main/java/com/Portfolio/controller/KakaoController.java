package com.Portfolio.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.service.MemberService;
import com.Portfolio.service.ProjectService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class KakaoController {

	@Autowired
	ProjectService projectService;
	
	@Autowired
	private final MemberService kakaoLoginService;
	// 1번 카카오톡에 사용자 코드 받기(jsp의 a태그 href에 경로 있음)
	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public String kakaoLogin(@RequestParam(value = "code", required = false) String code,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Throwable {

		// 코드확인
		System.out.println("code:" + code);
		// 사용자코드로 엑세스 토큰 발급받기
		String access_Token = kakaoLoginService.getAccessToken(code);
		//엑세스 토큰으로 사용자 정보확인
		HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(access_Token);
		//이메일 검증 하여 기존회원여부 확인
		MemberDTO member = kakaoLoginService.findByEmail("kakao_"+userInfo.get("nickname").toString());
		HttpSession session = request.getSession();
		if(member != null) {
		session.setAttribute("userInfo", member);
		}else {
			MemberDTO dto = MemberDTO.builder().email("kakao_"+userInfo.get("nickname").toString()).profileImg(userInfo.get("profileImg").toString()).name(userInfo.get("nickname").toString()).build();
			MemberDTO newMember = kakaoLoginService.register(dto);
			session.setAttribute("userInfo", newMember);
		} 
		return "redirect:index";

	}
	
	@GetMapping("myPage")
	public String myPage(Model model,HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberDTO dto = (MemberDTO) session.getAttribute("userInfo");
	 	if(dto == null || dto.getEmail() == null) {
	        try {
	            res.setContentType("text/html;charset=UTF-8");
	            PrintWriter out = res.getWriter();
	            out.println("<script>alert('로그인 후 이용 가능합니다.'); location.href='index';</script>");
	            out.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null; // null을 반환하여 뷰 페이지를 반환하지 않습니다.
	 	}else {
	 		MemberDTO member = kakaoLoginService.findByEmail(dto.getEmail());
	 		List<ProjectDTO> projectList = projectService.getList(dto.getEmail());
	 		model.addAttribute("user",member);
			model.addAttribute("projects", projectList);
			return "myPage";
	 	}
		
	}
	@PostMapping("introEdit")
	public String introEdit(MemberDTO dto) {
		kakaoLoginService.modify(dto);
		return "redirect:myPage";
	}
}