package com.first.task_manager.controllers.user;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.task_manager.repositories.UserDetailsRepository;
import com.first.task_manager.schema.Users;

@RestController
public class UserResource {
	@Autowired
	private UserDetailsRepository userRepository;
	
	@PostMapping("/user/currentUser")
	public ResponseEntity<?> getCurrentUser(){
//		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//		Users currentUser=userRepository.findByUsername(loggedInUsername);
//		return ResponseEntity.ok().body(currentUser);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    var loggedInUsername=SecurityContextHolder.getContext().getAuthentication().getName();
	    if (authentication instanceof OAuth2AuthenticationToken) {
	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
	        
	        loggedInUsername= (String) attributes.get("email");
	         
	    }
	    Optional<Users> currentUser=userRepository.findByUsername(loggedInUsername);
	    if(currentUser.isEmpty()) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }else {
	    	return ResponseEntity.ok().body(currentUser.get());
	    }
	}

}
