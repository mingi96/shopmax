package com.shopmax.entity;


import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test")
@ToString
@Getter
@Setter
public class Test {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long testId;
	
	@Column(nullable = false)
	private String loginid;
	
	@Column(nullable = true)
	private String password;
}
