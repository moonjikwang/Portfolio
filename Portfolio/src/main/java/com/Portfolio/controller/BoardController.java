package com.Portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.Portfolio.dto.BoardDTO;
import com.Portfolio.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	BoardService boardService;
	
	@GetMapping("board")
	public void board(Pageable pageable, Model model) {
		Page<BoardDTO> boards = boardService
				.getList(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
		model.addAttribute("boards", boards);
	}

}
