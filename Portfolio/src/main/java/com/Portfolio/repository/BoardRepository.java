package com.Portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Portfolio.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}
