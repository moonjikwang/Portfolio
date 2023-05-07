package com.Portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Portfolio.entity.Feedback;
import com.Portfolio.entity.Member;


public interface FeedbackRepository extends JpaRepository<Feedback,Long>{
	
	List<Feedback> findByWriter(Member writer);
}
