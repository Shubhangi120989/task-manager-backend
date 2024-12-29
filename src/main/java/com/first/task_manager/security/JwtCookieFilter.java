package com.first.task_manager.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtCookieFilter extends OncePerRequestFilter{
	
	private final JwtDecoder jwtDecoder;

    public JwtCookieFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;

    }
    
    
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // Extract JWT from cookies
//    	System.out.println("In the cookie fileter");
//        Cookie[] cookies = request.getCookies();
//        System.out.println(cookies);
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//            	System.out.println("Cookie Name: " + cookie.getName());
//                System.out.println("Cookie Value: " + cookie.getValue());
//                if ("jwt".equals(cookie.getName())) { // Look for the JWT cookie
//                    System.out.println("in the .equal wli cheez");
//                	try {
//                        var jwt = jwtDecoder.decode(cookie.getValue());
//                        System.out.println("Decoded JWT: " + jwt);
//                        var authorities = extractAuthoritiesFromJwt(jwt);
//                        
//                        var authentication = new JwtAuthenticationToken(jwt,authorities);
////                        authentication.setAuthenticated(true);
//                        System.out.println("JWT Token found in cookies."+ jwt);
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        System.out.println("authentication thing: "+authentication);
////                        var authentication2 = SecurityContextHolder.getContext().getAuthentication();
////                        System.out.println("Authentication Object: " + authentication2);
////                        System.out.println("Is authenticated: " + authentication2.isAuthenticated());
//
////                        System.out.println("set authorization also done."+ jwt);
//                    } catch (JwtException e) {
//                        // Handle invalid or expired JWT
//                    	 System.out.println("JWT Token not found in cookies.");
//                        SecurityContextHolder.clearContext();
//                    }
//                    break;
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("In the cookie filter");

        Cookie[] cookies = request.getCookies();
        System.out.println(cookies);

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName());
                System.out.println("Cookie Value: " + cookie.getValue());

                if ("jwt".equals(cookie.getName())) { // Check for JWT cookie
                    System.out.println("Processing JWT cookie...");
                    try {
                        var jwt = jwtDecoder.decode(cookie.getValue());
                        System.out.println("Decoded JWT: " + jwt);

                        var authorities = extractAuthoritiesFromJwt(jwt);

                        // Set JwtAuthenticationToken
                        var authentication = new JwtAuthenticationToken(jwt, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        System.out.println("JWT Authentication set: " + authentication);
                    } catch (JwtException e) {
                        System.out.println("Invalid or expired JWT token: " + e.getMessage());
                        SecurityContextHolder.clearContext();
                    }
                    break;
                } else if ("oauth2".equals(cookie.getName())) { // Check for OAuth2 token
                    System.out.println("Processing OAuth2 cookie...");
                    try {
                        // Assuming OAuth2 token processing, decode or validate if necessary
//                        String oauth2Token = cookie.getValue();
//                        
////                        var jwt = jwtDecoder.decode(oauth2Token); // Ensure your decoder supports this
//                        var jwt = jwtDecoder.decode(oauth2Token); // Ensure your decoder supports this
//                        
//                        // Extract attributes from the token
//                        Map<String, Object> attributes = Map.of(
//                            "sub", jwt.getSubject(), // Use 'sub' as user ID
//                            "email", jwt.getClaim("email"), // Assuming 'email' claim exists
//                            "name", jwt.getClaim("name")   // Assuming 'name' claim exists
//                        );
//
//                        // Create an OAuth2User implementation
//                        OAuth2User oAuth2User = new DefaultOAuth2User(
//                            List.of(new SimpleGrantedAuthority("ROLE_USER")), // Set user roles/authorities
//                            attributes, // Pass the extracted attributes
//                            "sub" // Specify the key for the principal name attribute
//                        );
//
//                        // Create an OAuth2AuthenticationToken
//                        var authentication = new OAuth2AuthenticationToken(
//                            oAuth2User, // The OAuth2User as principal
//                            oAuth2User.getAuthorities(), // Roles/Authorities
//                            "google" // Provider name (adjust based on your provider)
//                        );
//
//
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    	
                    	var jwt = jwtDecoder.decode(cookie.getValue());
                        System.out.println("Decoded JWT: " + jwt);

                        var authorities = extractAuthoritiesFromJwt(jwt);

                        // Set JwtAuthenticationToken
                        var authentication = new JwtAuthenticationToken(jwt, authorities);
                        System.out.println("OAuth2 Authentication set: " + authentication);
                    } catch (Exception e) {
                        System.out.println("Error processing OAuth2 token: " + e.getMessage());
                        SecurityContextHolder.clearContext();
                    }
                    break;
                }
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }


    
    private Collection<? extends GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // Assuming roles are stored as a claim named "roles"
        List<String> roles;
        roles=new ArrayList<>();
        roles.add(ALREADY_FILTERED_SUFFIX);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    
    
}
    


