package com.Portfolio.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;

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
	
	//사용자 이메일 중복체크 메서드
//	public boolean isDuplicateEmail(String email) {
//		return memberRepository.findByEmail(email).isPresent();
//	}
	
	//로그인
	@Transactional
	public Member login(String email) {
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if (optionalMember.isPresent()) {
			return optionalMember.get();
		}else {
			throw new RuntimeException("해당 사용자가 존재하지 않습니다.");
		}
	}
	
	//회원가입
	@Transactional
	public Member register(MemberDTO dto) {
		//사용자 비밀번호 해쉬 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		//사용자 이메일 중복체크
		Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
		if(optionalMember.isPresent()) {
			throw new RuntimeException("이미 사용중인 이메일입니다.");
		}
		//새로운 사용자 등록
		Member entity = dtoToEntity(dto);
	    return memberRepository.save(entity);
	}
	
}
