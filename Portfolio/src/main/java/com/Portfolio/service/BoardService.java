package com.Portfolio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.Portfolio.dto.BoardDTO;
import com.Portfolio.entity.Board;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.BoardRepository;
import com.Portfolio.repository.CommentsRepository;

@Service
public class BoardService {

	@Autowired
	BoardRepository boardRepository;
	@Autowired
	CommentsRepository commentsRepository;
	
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
	//상세 글 조회 메서드
	public BoardDTO findById(long bno) {
		Optional<Board> board = boardRepository.findById(bno);
		BoardDTO dto = entityToDto(board.get());
		return dto;
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
				.email(entity.getWriter().getEmail())
				.regDate(entity.getRegDate())
				.modDate(entity.getModDate())
				.viewCount(entity.getViewCount())
				.commentsCount(entity.getCommentsCount())
				.build();
	}
	//=====================END========================
	
	//====================START=======================
	//DTO -> Entity 변환해주는 메서드
	private Board dtoToEntity(BoardDTO dto) {
		return Board.builder()
				.bno(dto.getBno())
				.title(dto.getTitle())
				.content(dto.getContent())
				.viewCount(dto.getViewCount())
				.writer(Member.builder().email(dto.getEmail()).build())
				.build();
				
	}
	//=====================END========================
	
	//====================START=======================
	//게시글 저장
	public Long register(BoardDTO dto) {
		Board board = dtoToEntity(dto); 
		boardRepository.save(board);
		return board.getBno();
	}
	//=====================END========================
	
	//====================START=======================
	//게시글 삭제
	@Transactional
	public void removeWithComments(Long bno) {
		
		commentsRepository.deleteBybno(bno);
		boardRepository.deleteById(bno);
	}
	//=====================END========================
	
	//====================START=======================
	//게시글 수정
	@Transactional
	public void modify(BoardDTO boardDTO) {
		
		Board board = boardRepository.findById(boardDTO.getBno()).get();
		
		board.changeTitle(boardDTO.getTitle());
		board.changeContent(boardDTO.getContent());
		boardRepository.save(board);
	}
	//=====================END========================
	
	//====================START=======================
	//조회수 세팅
	@Transactional
	public Long viewCount(Long bno) {
		Board board = boardRepository.findById(bno).get();
		board.setViewCount(board.getViewCount()+1);
		boardRepository.save(board);
		return null;
	}

	//=====================END========================




}
