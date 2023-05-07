package com.Portfolio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Portfolio.dto.FeedbackDTO;
import com.Portfolio.dto.MemberDTO;
import com.Portfolio.dto.PortfolioDTO;
import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.repository.FeedbackRepository;
import com.Portfolio.service.ApiService;
import com.Portfolio.service.FeedbackService;
import com.Portfolio.service.MemberService;
import com.Portfolio.service.PortfolioService;
import com.Portfolio.service.ProjectService;

@Controller
public class PortfolioController {
	
	@Autowired
	ProjectService projectService;
	@Autowired
	PortfolioService portfolioService;
	@Autowired
	ApiService apiService;
	@Autowired
	MemberService memberService;
	@Autowired
	FeedbackService feedbackService;

	@GetMapping("")
	public String root() {
		return "redirect:index";
	}
	@GetMapping("index")
	public void index(Model model,Pageable pageable) {
		Page<PortfolioDTO> portfolios = portfolioService.getList(PageRequest.of(pageable.getPageNumber(), 8, Sort.by("regDate").descending()));
		model.addAttribute("portfolios",portfolios);
	}
	
	@PostMapping("addProject")
	public String addProject(@RequestParam("image") MultipartFile image,ProjectDTO dto, HttpServletResponse res, HttpServletRequest req) {
		String imgUrl = postImage(image,res,req);
		dto.setImgUrl(imgUrl);
		projectService.addProject(dto);
	//저장하기
		return "redirect:myPage";
	}
	
	public String postImage(MultipartFile upload, HttpServletResponse res, HttpServletRequest req){
        OutputStream out = null;
        PrintWriter printWriter = null;
        String fileUrl = null;
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");
		try{

            UUID uuid = UUID.randomUUID();
            String extension = FilenameUtils.getExtension(upload.getOriginalFilename());

            byte[] bytes = upload.getBytes();
            // 실제 이미지 저장 경로
            String path =  req.getSession().getServletContext().getRealPath("/");
        	String folderPath = path+ File.separator+"image"; //폴더 경로
        	File Folder = new File(folderPath);
        	// 해당 디렉토리가 없다면 디렉토리를 생성.
        	if (!Folder.exists()) {
        		try{
        		    Folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
        		    System.out.println("폴더가 생성완료.");
        	        } 
        	        catch(Exception e){
        		    e.getStackTrace();
        		}        
                 }else {
        		System.out.println("폴더가 이미 존재합니다..");
        	}
            
            String imgUploadPath = path + File.separator+"image" + File.separator+ uuid + "." + extension;

            // 이미지 저장
            out = new FileOutputStream(imgUploadPath);
            out.write(bytes);
            out.flush();
           fileUrl = "http://tomcat.jikwang.net/Portfolio/image/" + uuid + "." + extension;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null) { out.close(); }
                if(printWriter != null) { printWriter.close(); }
            } catch(IOException e) { e.printStackTrace(); }
        }
		return fileUrl;
    }
	@GetMapping("Portfolio")
	public void portfolio(String email,Model model) {
		MemberDTO member = memberService.findByEmail(email);
		List<ProjectDTO> projects = projectService.getList(email);
		portfolioService.viewCount(email);
		model.addAttribute("member",member);
		model.addAttribute("projects",projects);
	}
	
	@GetMapping("feedback")
	public void feedback(String email,Model model) {
		MemberDTO member = memberService.findByEmail(email);
		List<ProjectDTO> projects = projectService.getList(email);
		List<FeedbackDTO> feedbacks = feedbackService.getList(email);
		portfolioService.viewCount(email);
		model.addAttribute("feedbacks",feedbacks);
		model.addAttribute("member",member);
		model.addAttribute("projects",projects);
	}
	@PostMapping("feedbackWrite")
	public String feedbackWrite(FeedbackDTO dto,RedirectAttributes redirectAttributes) {
		feedbackService.writeFeedback(dto);
		redirectAttributes.addAttribute("email",dto.getEmail());
		return "redirect:feedback";
	}
	@GetMapping("removeFeedback")
	public String deleteFeedback(Long no,String email,RedirectAttributes redirectAttributes) {
		feedbackService.removeFeedback(no);
		redirectAttributes.addAttribute("email",email);
		return "redirect:feedback";
	}

	
	//체인지로그 컨트롤
	@GetMapping("/changelog")
	public void changelog(Model model) {
		List<Map<String, String>> committer = apiService.apicall();
		model.addAttribute("list",committer);
	}
	
}
