package com.Portfolio.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.UserSessionDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final HttpSession session;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//사용자 정보 조회(이메일 검증하여 기존회원여부 확인)
		Optional<Member> member = memberRepository.findByEmail(email);
		
		if(member.isPresent()) {
			session.setAttribute("member", new UserSessionDTO(member.get()));
			//시큐리티 세션에 유저 정보 저장
			return new CustomUserDetails(member.get()); 
		}else {
			throw new UsernameNotFoundException("해당 이메일이 존재하지 않습니다. email : " + email);
		}
	}
}
