package com.first.task_manager.security;

import org.springframework.web.bind.annotation.RestController;

import com.first.task_manager.repositories.UserDetailsRepository;
import com.first.task_manager.schema.Users;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
//import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.hibernate.boot.jaxb.JaxbLogger_.logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class JwtAuthenticationResource {
private JwtEncoder jwtEncoder;
private AuthenticationManager authenticationManager;
private UserDetailsRepository userRepository;
@Value("${frontend.url}")
private String frontendUrl; 
	
	public JwtAuthenticationResource(JwtEncoder jwtEncoder,AuthenticationManager authenticationManager,UserDetailsRepository userReposiotry) {
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
		this.userRepository=userReposiotry;
	}
	
//	@PostMapping("/authenticate") 
//	public JwtResponse authenticate(Authentication authentication) {
//		return new JwtResponse(createToken(authentication));
//	}
	
	//to include token into the cookies
	@PostMapping("/authenticate")
	public ResponseEntity<JwtResponse> authenticate(HttpServletResponse response,@RequestParam String username,@RequestParam String password) {
		var authenticationToken = 
                new UsernamePasswordAuthenticationToken(
                        username, 
                        password);
		var authentication = authenticationManager.authenticate(authenticationToken);
        
//        var authentication = 
//                authenticationManager.authenticate(authenticationToken);
	    String token = createToken(authentication);

	    // Create a secure, HTTP-only cookie
	    ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
	            .httpOnly(true)
	            .secure(true) // Use secure cookies (only transmitted over HTTPS)
	            .path("/") // Make it available for all endpoints
	            .maxAge(60 * 60 * 3) // Set expiration (3 hours)
	            .sameSite("None") // Prevent CSRF attacks
	            .build();

	    // Add the cookie to the response header
	    response.addHeader("Set-Cookie", jwtCookie.toString());

	    // Optionally return the token in the response body (optional)
	    return ResponseEntity.ok(new JwtResponse(token));
	
	}
	
	@GetMapping("/success")
	public void handleGoogleLogin(HttpServletResponse response) throws IOException {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    if (authentication instanceof OAuth2AuthenticationToken) {
	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
	        
	        String email = (String) attributes.get("email");
	        String name = (String) attributes.get("name");
	        String avatarUrl = (String) attributes.get("picture");
	        
	        // Find user by email (username)
	        Optional<Users> userFound = userRepository.findByUsername(email);
	        
	        if (userFound.isEmpty()) {
	            // User doesn't exist, create a new one
	            Users user = new Users();
	            user.setUsername(email);
	            user.setName(name);
	            user.setAvatarUrl(avatarUrl);
	            user.setBio(""); // Default bio, or set as needed
	            user.setEnabled(true);
	            userRepository.save(user);
	        } else {
	            // User exists, update any missing fields
	            if (userFound.get().getName() == null) userFound.get().setName(name);
	            if (userFound.get().getAvatarUrl() == null) userFound.get().setAvatarUrl(avatarUrl);
	            userRepository.save(userFound.get());
	        }

	        // Generate JWT token (handled in your current logic)
	        String token = createToken(authentication);
	        
	        // Create a cookie with the token
	        ResponseCookie jwtCookie = ResponseCookie.from("oauth2", token)
	                .httpOnly(true)
	                .secure(true) // Use secure cookies (only transmitted over HTTPS)
	                .path("/") // Make it available for all endpoints
	                .maxAge(60 * 60 * 3) // Set expiration (3 hours)
	                .sameSite("None") // Prevent CSRF attacks
	                .build();

	        // Add the cookie to the response header
	        response.addHeader("Set-Cookie", jwtCookie.toString());

	        // Redirect the user to the frontend (specific URL)
	        String redirectUrl = frontendUrl + "/projects";
	        response.sendRedirect(redirectUrl); // Redirect to your frontend
	    } else {
	        // Instead of sending an error, redirect to a custom error page or return an error response
	        response.sendRedirect("/error"); // Redirect to a custom error page
	    }
	}

	
	
	private String createToken(Authentication authentication) {
		Instant expiresAt = Instant.now().plusSeconds(60 * 180); // Add 3 hours
	    System.out.println("Token expires at: " + expiresAt);
		
//	    logger.info("Token expiration time: {}", jwt.getClaim("exp"));
//	    logger.info("Current server time: {}", Instant.now());

	    
		var claims = JwtClaimsSet.builder()
								.issuer("self")
								.issuedAt(Instant.now())
								.expiresAt((expiresAt))
								.subject(authentication.getName())
								.claim("scope", createScope(authentication))
								.build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims))
							.getTokenValue();
	}

	private String createScope(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(a -> a.getAuthority())
			.collect(Collectors.joining(" "));			
	}

}

record JwtResponse(String token) {}
