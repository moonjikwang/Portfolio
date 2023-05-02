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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.repository.ProjectRepository;
import com.Portfolio.service.ApiService;
import com.Portfolio.service.ProjectService;

@Controller
public class PortfolioController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ApiService apiService;

	@GetMapping("")
	public String root() {
		return "redirect:index";
	}
	@GetMapping("index")
	public void index() {
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
	
	//체인지로그 컨트롤
	@GetMapping("/changelog")
	public void changelog(Model model) {
		List<Map<String, String>> committer = apiService.apicall();
		model.addAttribute("list",committer);
	}
	
}
