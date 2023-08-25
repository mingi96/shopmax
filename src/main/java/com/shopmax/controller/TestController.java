package com.shopmax.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopmax.entity.Test;
import com.shopmax.service.TestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {
	
	private final TestService testService;
	
	@GetMapping(value = "/test")
	public String test() {
		return "/test";
	}
	
	@PostMapping(value = "/test")
	public String test(@Valid String id, @Valid String password, Model model) {
		testService.insert(id, password);
		return "/test";
	}
	
	@GetMapping(value = "/test/login")
	public String testLogin(Model model) {
		
		return "/testLogin";
	}
	
	@PostMapping(value = "/test/login")
	public String testLogin(Model model, @Valid String id, @Valid String password) {
			Test test = testService.login(id, password);
		
		if(test == null) {
			model.addAttribute("errorMessage", "로그인 실패! 아이디 또는 비밀번호 확인");
			return "/testLogin";
		}
		
		model.addAttribute("errorMessage", "로그인 성공!");
		return "/testLogin";
	}
}
