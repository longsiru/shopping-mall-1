package com.myshop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.entity.Member;
import com.myshop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service  //service클래스의 역할
@Transactional  //서비스 클래스에서 로직을 처라하다가 에러가 발생하명 로직
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{ //UserDetailsService:로그인시 require에서 넘어온 사용자 정보를 받음
	private final MemberRepository memberRepository; // field의존성 주입
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			Member member = memberRepository.findByEmail(email);
			
			if(member == null) {
				throw new UsernameNotFoundException(email);
			}
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
	
	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member); //member table insert
	}
	
	//email 중복체크 메소드
	private void validateDuplicateMember(Member member) {
		Member findMember= memberRepository.findByEmail(member.getEmail());
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
		
	}

	
	
}