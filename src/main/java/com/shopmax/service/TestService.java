package com.shopmax.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopmax.entity.Test;
import com.shopmax.repository.TestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {
	private final TestRepository testRepository;
	
	public void insert(String id, String password) {
		Test test = new Test();
		
		test.setLoginid(id);
		test.setPassword(password);
		
		testRepository.save(test);
	}
	
	public Test login(String id, String password) {
		Test test = testRepository.findByLoginidAndPassword(id, password);
		
		return test;
	}
}
