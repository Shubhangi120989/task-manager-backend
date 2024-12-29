package com.first.task_manager.controllers.user;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

@Service
class TokenBlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}

@RestController
public class LogoutController {
	
	 @PostMapping("/user/logout")
	    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
	        System.out.println("In the logout controller");

	        // List of cookie names to be cleared
	        String[] cookiesToClear = {"jwt", "oauth2", "JSESSIONID"};

	        // Iterate through the cookies and clear them
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                String cookieName = cookie.getName();
	                if (Arrays.asList(cookiesToClear).contains(cookieName)) {
	                    System.out.println("Clearing cookie: " + cookieName);

	                    // Create an expired cookie
	                    ResponseCookie expiredCookie = ResponseCookie.from(cookieName, "")
	                            .httpOnly(true)
	                            .secure(true) // Ensure the cookie is only sent over HTTPS
	                            .path("/") // Ensure the path matches the original cookie
	                            .maxAge(0) // Expire the cookie immediately
	                            .sameSite("None") // Match the SameSite policy of the original cookie
	                            .build();

	                    // Add the expired cookie to the response
	                    response.addHeader("Set-Cookie", expiredCookie.toString());
	                }
	            }
	        }

	        // Clear the security context
	        SecurityContextHolder.clearContext();

	        // Return a response indicating logout was successful
	        return ResponseEntity.ok().body("Logged out successfully");
	    }

}
