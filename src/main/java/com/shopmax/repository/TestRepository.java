package com.shopmax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopmax.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long>{
	Test findByLoginidAndPassword(String id, String password);
}
