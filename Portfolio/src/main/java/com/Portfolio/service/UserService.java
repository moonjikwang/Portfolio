package com.Portfolio.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;
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
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final HttpSession httpSession;

	public Member dtoToEntity(MemberDTO dto) {
		Member entity = Member.builder()
				.email(dto.getEmail())
				.password(dto.getPassword())
				.name(dto.getName())
				.nickName(dto.getNickName())
				.profileImg(dto.getProfileImg())
				.build();
		return entity;
	}
	
	//로그인
	@Transactional
	public Member login(MemberDTO dto) {
		//이메일 검증하여 기존회원여부 확인
		Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
		
		if (optionalMember.isPresent()) {
			//이메일이 존재할 경우 -> 세션에 로그인 유저 정보 저장
			Member member = optionalMember.get();
			if (encoder.matches(dto.getPassword(),member.getPassword())) {
				httpSession.setAttribute("userInfo", member);
			} else {
				throw new RuntimeException("비밀번호가 일치하지 않습니다.");
			}
			return optionalMember.get();
		}else {
			//해당 이메일이 존재하지 않을 경우 -> 세션에 로그인 실패 이메일 정보 저장
			httpSession.setAttribute("loginFailEmail", dto.getEmail());
			throw new RuntimeException("해당 사용자가 존재하지 않습니다.");
		}
	}
	
	//회원가입
	@Transactional
	public void register(MemberDTO dto) {
		//사용자 비밀번호 해쉬 암호화
		dto.setPassword(encoder.encode(dto.getPassword()));
		//사용자 이메일 중복체크
		Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
		if(optionalMember.isPresent()) {
			throw new RuntimeException("이미 사용중인 이메일입니다.");
		}
		//새로운 사용자 등록
		Member entity = dtoToEntity(dto);
		memberRepository.save(entity);
		httpSession.setAttribute("userInfo", dto);
	}
}
