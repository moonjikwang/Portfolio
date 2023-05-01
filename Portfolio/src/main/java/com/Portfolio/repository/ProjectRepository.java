package com.Portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Portfolio.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

	List<Project> findByWriter_Email(String email);
}
