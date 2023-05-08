package com.Portfolio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Portfolio.dto.BoardDTO;
import com.Portfolio.dto.CommentsDTO;
import com.Portfolio.dto.MemberDTO;
import com.Portfolio.service.BoardService;
import com.Portfolio.service.CommentsService;

@Controller
public class BoardController {

	@Autowired
	BoardService boardService;
	@Autowired
	CommentsService commentsService;
	
//	@ExceptionHandler(NoSuchElementException.class)
//	@ResponseStatus
//	public String handleNoSuchElementException(HttpServletRequest request, Exception e) {
//	    // 404 예외 발생 시 예외 페이지로 이동
//	    return "redirect:board";
//	}
	
	//게시판 페이지
	@GetMapping("/board")
	public void board(Pageable pageable, Model model) {
		Page<BoardDTO> boards = boardService
				.getList(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
		model.addAttribute("boards", boards);
	}
	
	//검색
    @GetMapping("/search")
    public String search(Pageable pageable, @RequestParam String searchType, @RequestParam String keyword, Model model) {
    	Page<BoardDTO> boards;
        if (searchType != null && keyword != null) {
            boards = boardService.search(searchType, keyword, PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
        } else {
            boards = boardService.getList(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
        }
        model.addAttribute("boards", boards);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        
        return "board";
    }
	
	//게시글 상세보기
	@GetMapping({"boardRead"})
	public void  read(long bno, Pageable pageable, Model model) {
		BoardDTO dto = boardService.findById(bno);
		List<CommentsDTO> commentsDTOs = commentsService.getList(bno);
		boardService.viewCount(bno); //views ++
		Page<BoardDTO> boards = boardService
				.getList(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));

		model.addAttribute("dto",dto);
		model.addAttribute("comments",commentsDTOs);
		model.addAttribute("boards", boards);
	}
	@GetMapping({"boardModify"})
	public String  modify(long bno,HttpServletRequest req,HttpServletResponse res, Pageable pageable, Model model) {
		HttpSession session = req.getSession();
		PrintWriter printWriter = null;
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");
		MemberDTO member = (MemberDTO) session.getAttribute("userInfo");
		BoardDTO dto = boardService.findById(bno);
		if(member == null || dto.getEmail() != member.getEmail()) {
			try {
				printWriter = res.getWriter();
	            printWriter.println("<script type='text/javascript'>"
	                    + "alert('권한이 없습니다.');"
	                    + "history.back();"
	                    +"</script>");
	            printWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
	                if(printWriter != null) { printWriter.close(); }
	        }
		}
		List<CommentsDTO> commentsDTOs = commentsService.getList(bno);
		boardService.viewCount(bno); //views ++
		Page<BoardDTO> boards = boardService
				.getList(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));

		model.addAttribute("dto",dto);
		model.addAttribute("comments",commentsDTOs);
		model.addAttribute("boards", boards);
		return "boardModify";
	}
	
	//새글쓰기 페이지로 매핌
	@GetMapping("boardWrite")
	public void boardWrite() {
		
	}
	
	//게시글 작성하기
	@PostMapping("boardWrite")
	public String boardWrite(BoardDTO dto,RedirectAttributes redirectAttributes) {
		System.out.println("=========입력받은값 :"+dto);
		Long bno = boardService.register(dto);
		redirectAttributes.addAttribute("bno",bno);
		return "redirect:boardRead";
	}
	
	//게시글 수정하기
	@PostMapping("boardModify")
	public String boardModify(BoardDTO dto,RedirectAttributes redirectAttributes) {
		System.out.println("=========입력받은값 :"+dto);
		boardService.modify(dto);
		redirectAttributes.addAttribute("bno", dto.getBno());
		return "redirect:boardRead";
	}
	
	//게시글 삭제
	@GetMapping("boardDelete")
	public String boardDelete(long bno,HttpServletRequest req,HttpServletResponse res) {
		HttpSession session = req.getSession();
		PrintWriter printWriter = null;
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");
		MemberDTO member = (MemberDTO) session.getAttribute("userInfo");
		BoardDTO dto = boardService.findById(bno);
		if(member == null || dto.getEmail() != member.getEmail()) {
			try {
				printWriter = res.getWriter();
	            printWriter.println("<script type='text/javascript'>"
	                    + "alert('권한이 없습니다.');"
	                    + "history.back();"
	                    +"</script>");
	            printWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
	                if(printWriter != null) { printWriter.close(); }
	        }
		}else {
		boardService.removeWithComments(bno);
		}
		return "redirect:board";
	}
	
	//댓글 달기
	@PostMapping("boardCommentWrite")
	public String commentWrite(CommentsDTO dto,RedirectAttributes redirectAttributes) {
		commentsService.register(dto);
		Long bno = dto.getBno();
		
		redirectAttributes.addAttribute("bno",bno);
		return "redirect:boardRead";
	}
	
	//댓글 삭제
	@GetMapping("boardCommentRemove")
	public String commentRemove(Long cno,Long bno,RedirectAttributes redirectAttributes) {
		commentsService.remove(cno);
		redirectAttributes.addAttribute("bno",bno);
		return "redirect:boardRead";
	}
	
	//파일업로드
	@PostMapping("fileUpload")
    public void postImage(MultipartFile upload, HttpServletResponse res, HttpServletRequest req){

        OutputStream out = null;
        PrintWriter printWriter = null;

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

            // ckEditor 로 전송
            printWriter = res.getWriter();
            String callback = req.getParameter("CKEditorFuncNum");
            String fileUrl = "https://tomcat.jikwang.net/Portfolio/image/" + uuid + "." + extension;

            printWriter.println("<script type='text/javascript'>"
                    + "window.parent.CKEDITOR.tools.callFunction("
                    + callback+",'"+ fileUrl+"','이미지를 업로드하였습니다.')"
                    +"</script>");

            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null) { out.close(); }
                if(printWriter != null) { printWriter.close(); }
            } catch(IOException e) { e.printStackTrace(); }
        }
    }

}
