package com.myshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.myshop.service.MemberService;

@Configuration  //스프링에서 설정 클레스로 사용하겠다.
@EnableWebSecurity  //springSecurityFilterChain이 자동으로 포함됨.
public class SecurityConfig { //extends WebSecurityConfigurerAdapter 

	//http요청에 대해 버안을 설정한다.페이지 권한 설덩 , 로그인 페이지 설정 , 로그아웃 메소드 등에 다한 살정을 한다
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		
//	}
//	
	@Autowired
	MemberService memberServise;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//로그인에 대한 설정
		http.formLogin()
			 .loginPage("/members/login") //로그인 페이지 url설정
			 .defaultSuccessUrl("/") //로그인 설공시 이동할 페이지
			 .usernameParameter("email")//로그인 사용할 파리메터 이름
			 .failureUrl("/members/login/error") //로그인 실패시 이동항 url설정
			 .and()
			 .logout()
			 .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))  //로그아웃 url
			 .logoutSuccessUrl("/"); //로그아웃 성공시 이동할 url
		
		
		//페이지 접근에 관한 설정
		http.authorizeRequests()
		.mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
		.mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll() //모든 사용자가 로그인 없이 접근 할 수 없어
		.mvcMatchers("/admin/**").hasRole("ADMIN") //'admin'으로 시작하는 경로는 계정이 ADMIN role일 경우에만 접근가능하도록 설전
		.anyRequest().authenticated(); //그외에 페이지는 모두 로그인(인증)을 받아야 한다
		
		//인증되지 않는 사용자가 리소스(페이지,이미지 등 )이 접근했을때 설정
		http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
			
			
			
		return http.build();
		
		
	}
	
	//비밀번호 암호화 해서 정장
	@Bean
	public PasswordEncoder passwordEncoder() { //비밀번호 암호화를 위해서 사용하는 빈(bean)
		return new BCryptPasswordEncoder();
	}

	
	
}
