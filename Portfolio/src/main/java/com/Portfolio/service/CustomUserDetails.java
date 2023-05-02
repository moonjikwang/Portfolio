package com.Portfolio.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Portfolio.entity.Member;

public class CustomUserDetails implements UserDetails {

	public Member member;
	
	public CustomUserDetails(Member member) {
		
	}

	/* 멤버 권한 목록 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getName();
	}

	/* 계정 만료 여부
     * true : 만료 안됨 / false : 만료
     */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/* 계정 잠김 여부
     *  true : 잠기지 않음 / false : 잠김
     */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/* 비밀번호 만료 여부
     *  true : 만료 안됨 / false : 만료
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* 사용자 활성화 여부
     *  true : 만료 안됨 / false : 만료
     */
	@Override
	public boolean isEnabled() {
		return true;
	}

}