package com.Portfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Portfolio.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

	Page<Board> findByWriter_EmailContainingIgnoreCaseOrderByBnoDesc(String keyword, Pageable pageable);

	Page<Board> findByTitleContainingIgnoreCaseOrderByBnoDesc(String keyword, Pageable pageable);

	Page<Board> findByContentContainingIgnoreCaseOrderByBnoDesc(String keyword, Pageable pageable);
	
	@Query(value = "SELECT * FROM board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR b.writer_email LIKE %:keyword% ORDER BY DESC", nativeQuery = true)
	Page<Board> searchByTitleOrContentOrWriter(@Param("keyword") String keyword, Pageable pageable);


}
