package com.Portfolio.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Portfolio.dto.BoardDTO;
import com.Portfolio.dto.CommentsDTO;
import com.Portfolio.service.BoardService;
import com.Portfolio.service.CommentsService;

@Controller
public class BoardController {

	@Autowired
	BoardService boardService;
	@Autowired
	CommentsService commentsService;
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus
	public String handleNoSuchElementException(HttpServletRequest request, Exception e) {
	    // 404 예외 발생 시 예외 페이지로 이동
	    return "redirect:board";
	}
	
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
	@GetMapping({"boardRead","boardModify"})
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
	
	//게시글 작성하기
	@PostMapping("boardWrite")
	public String boardWrite(BoardDTO dto,RedirectAttributes redirectAttributes) {
		System.out.println("=========입력받은값 :"+dto);
		Long bno = boardService.register(dto);
		redirectAttributes.addAttribute("bno",bno);
		return "redirect:boardRead";
	}
	
	//새글쓰기 페이지로 매핌
	@GetMapping("boardWrite")
	public void boardWrite() {
		
	}
	
	//게시글 삭제
	@GetMapping("boardDelete")
	public String boardDelete(long bno) {
		boardService.removeWithComments(bno);
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

}
