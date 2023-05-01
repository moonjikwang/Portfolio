package com.Portfolio.service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.MemberRepository;

@Service
public class UserService {

	@Autowired
	public MemberRepository memberRepository;
	public HttpSession session;
	public BCryptPasswordEncoder encoder; 
	
	private MemberDTO entityToDTO(Member member) {
		MemberDTO dto = MemberDTO.builder()
				.email(member.getEmail())
				.password(member.getPassword())
				.name(member.getName())
				.nickName(member.getNickName())
				.profileImg(member.getProfileImg())//
				.build();
		return dto;
	}
	
	private Member dtoToEntity(MemberDTO dto) {
		Member entity = Member.builder()
				.email(dto.getEmail())
				.password(dto.getPassword())
				.name(dto.getName())
				.nickName(dto.getNickName())
				.profileImg(dto.getProfileImg())
				.build();
		return entity;
	}
	
	public MemberDTO findByEmail(String email) {
		Member member = memberRepository.getMemberWithEmail(email);
		if(member != null) {
			return entityToDTO(member);
		}else {
			return null;
		}
	}
	
	@Transactional
	public String register(MemberDTO dto) {
		Member member = dtoToEntity(dto);
		memberRepository.save(member);
		return member.getEmail();
	}
	
}
