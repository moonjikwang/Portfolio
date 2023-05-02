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

	Page<Board> findByWriter_EmailContainingIgnoreCaseOrTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByBnoDesc(String writer, String title, String content, Pageable pageable);


}
