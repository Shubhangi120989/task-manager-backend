package com.first.task_manager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
	@GetMapping("hello-world")
	public String helloWorld() {
		return "hello World";
	}
	
	@PostMapping("hello-world")
	public String h() {
		return "Hello";
	}

}
