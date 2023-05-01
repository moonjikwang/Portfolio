package com.Portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Portfolio.entity.Board;
import com.Portfolio.entity.Comments;


public interface CommentsRepository extends JpaRepository<Comments, Long>{
	//Blind게시물 삭제시 댓글 삭제
	@Modifying
	@Query("delete from Comments c where c.board.bno =:bno ")
	void deleteBybno(@Param("bno")Long bno);
	
	//게시물로 댓글 가져오기
	//List<BlindComments> findByBlind(Long blind_no);
	
	//댓글목록
	List<Comments> getCommentsByBoard(Board board);
}
