package com.Portfolio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.CommentsDTO;
import com.Portfolio.entity.Board;
import com.Portfolio.entity.Comments;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.CommentsRepository;

@Service
public class CommentsService {

	@Autowired
	CommentsRepository commentsRepository;
	
	//====================START=======================
	//Entity -> DTO 변환해주는 메서드
	private CommentsDTO entityToDto(Comments comments) {
		CommentsDTO dto = CommentsDTO.builder()
				.cno(comments.getCno())
				.text(comments.getText())
				.replyer(comments.getReplyer())
				.bno(comments.getBoard().getBno())
				.email(comments.getWriter().getEmail())
				.profileImg(comments.getWriter().getProfileImg())
				.regDate(comments.getRegDate())
				.modDate(comments.getModDate())
				.build();
		return dto;
	}
	//=====================END========================
	
	//====================START=======================
	//DTO -> Entity 변환해주는 메서드
	private Comments dtoToEntity(CommentsDTO dto) {
		Comments comments = Comments.builder()
				.cno(dto.getCno())
				.text(dto.getText())
				.replyer(dto.getReplyer())
				.board(Board.builder().bno(dto.getBno()).build())
				.writer(Member.builder().email(dto.getEmail()).build())
				.build();
		return comments;
				
	}
	//=====================END========================
	
	
	//====================START=======================
	//댓글 리스트 조회
	public List<CommentsDTO> getList(Long bno) {
		List<Comments> result = commentsRepository
				.getCommentsByBoard(Board.builder().bno(bno).build());
		
		return result.stream().map(comments -> entityToDto(comments))
				.collect(Collectors.toList());
	}
	
	//=====================END========================
	
	
	//====================START=======================
	//댓글 등록
	public Long register(CommentsDTO commentsDTO) {
		Comments comments = dtoToEntity(commentsDTO);
		commentsRepository.save(comments);
		return comments.getCno();
	}
	//=====================END========================
	
	
	//====================START=======================
	//댓글 삭제
	public void remove(Long cno) {
		commentsRepository.deleteById(cno);
	}
	//=====================END========================
	
	//====================START=======================
	//댓글 수정
	public void modify(CommentsDTO commentsDTO) {
		Comments comments = dtoToEntity(commentsDTO);
		commentsRepository.save(comments);
	}
	//=====================END========================
	
	//====================START=======================
	//댓글 1개 조회
	public CommentsDTO getDTO(Long cno) {
		return entityToDto(commentsRepository.findById(cno).get());
	}
	//=====================END========================
	
	
}
