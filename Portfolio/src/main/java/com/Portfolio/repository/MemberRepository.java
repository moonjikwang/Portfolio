package com.Portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Portfolio.entity.Member;


public interface MemberRepository extends JpaRepository<Member, String>{
	@Query("SELECT a FROM Member a WHERE a.email =:email")
	Member getMemberWithEmail(@Param("email")String email);
		Optional<Member> findByEmail(String email);
	
	
	
	List<Member> findByName(String name);
	Page<Member> findByStateTrue(Pageable pageable);
}
