package com.Portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.httpBasic().disable();
    httpSecurity.csrf().disable();
    httpSecurity
    			.formLogin()
    				.loginPage("/login")
    				.defaultSuccessUrl("/")
    			.and()
    			.logout()
    				.logoutSuccessUrl("/login")
    				.invalidateHttpSession(true)
    				;
  }
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
	  return new BCryptPasswordEncoder();
  }
  
  
}