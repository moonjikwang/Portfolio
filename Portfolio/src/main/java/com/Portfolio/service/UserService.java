package com.Portfolio.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.dto.UserSessionDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final MemberRepository memberRepository;
	private final HttpSession session;
	private final BCryptPasswordEncoder encoder;

	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.getMemberWithEmail(email);
        if (member != null) {
            // 시큐리티 세션에 유저 정보 저장
            session.setAttribute("member", new UserSessionDTO(member));
            return new CustomUserDetails(member);
        } else {
            session.setAttribute("member", null);
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + email);
        }
    }
	
	// 이메일 중복 체크
	public MemberDTO findByEmail(String email) {
	    Member member = memberRepository.getMemberWithEmail(email);
	    if (member != null) {
	        session.setAttribute("member", new UserSessionDTO(member));
	        return entityToDTO(member);
	    } else {
	        session.setAttribute("member", null);
	        return null;
	    }
	}

	
	// 여기부터.
	// 1. 사용자생성
	@Transactional
	public Member create(final Member member) {
		if(member == null || member.getEmail() == null) {
			throw new RuntimeException("해당 사용자가 존재하지 않습니다. : " + member);
		}
		final String email = member.getEmail();
		if(memberRepository.existByEmail(email)) {
			log.warn("이미 존재하는 이메일입니다.");
			throw new RuntimeException("이미 존재하는 이메일입니다.");
		}
		return memberRepository.save(member);
	}
	
	// 2. email & password로 사용자 확인
	public Member getBycredential(final String email, final String password) {
		return memberRepository.findByEmailAndPassword(email, password);
	}
	
	
	
	

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

	
}
