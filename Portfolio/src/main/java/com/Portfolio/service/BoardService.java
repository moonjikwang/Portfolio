package com.Portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.Portfolio.dto.BoardDTO;
import com.Portfolio.entity.Board;
import com.Portfolio.repository.BoardRepository;

@Service
public class BoardService {

	@Autowired
	BoardRepository boardRepository;
	//====================START=======================
	//게시글 목록 전체 불러오는 메서드
	public Page<BoardDTO> getList(Pageable pageable){
		 Page<Board> page = boardRepository.findAll(pageable);
	     List<BoardDTO> boardList = new ArrayList<>();
	     page.forEach(entity -> {
	    	 BoardDTO boardDTO = entityToDto(entity);
	    	 boardList.add(boardDTO);
	     		});
		Page<BoardDTO> pageDto = new PageImpl<>(boardList, pageable, page.getTotalElements());
		return pageDto;
	}
	//=====================END========================
	
	//====================START=======================
	//Entity -> DTO 변환해주는 메서드
	private BoardDTO entityToDto(Board entity) {
		return BoardDTO.builder()
				.bno(entity.getBno())
				.title(entity.getTitle())
				.content(entity.getContent())
				.name(entity.getWriter().getName())
				.regDate(entity.getRegDate())
				.modDate(entity.getModDate())
				.viewCount(entity.getViewCount())
				.build();
	}
	//=====================END========================
}
