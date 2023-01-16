package com.myshop.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myshop.dto.MemberFormDto;
import com.myshop.entity.Member;
import com.myshop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("members")
@Controller
@RequiredArgsConstructor
public class MenberController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder ;
	
	
	@GetMapping(value = "/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	@PostMapping(value = "/new")
	public String memberForm(@Valid MemberFormDto memberFormDto,  BindingResult bindingResult, Model model) {
		//@Valid: 유효성을 검증하려는 객체 앞에 붙인다.
		//BindingResult: 유효성 검증후 결과을 넣어준다. 
		
		//error ---> 회원가입 페이지
		if(bindingResult.hasErrors()) {
			return "member/memberForm";
		}
		
		
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
			
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		
		return "redirect:/";
	}

}
