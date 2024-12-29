package com.first.task_manager.security;



import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

//import jakarta.servlet.Filter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class JwtSecurityConfiguration {
	@Value("${frontend.url}")
    private String frontendUrl; 
	
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 
		 JwtDecoder jwtDecoder = jwtDecoder(null); 
		 http.addFilterBefore(new JwtCookieFilter(jwtDecoder), UsernamePasswordAuthenticationFilter.class);

		 
		 
	        http.authorizeHttpRequests(auth -> auth
	                .requestMatchers("/register","/authenticate","/oauth2/**","/success/**").permitAll() // Allow access to the registration endpoint
	                .anyRequest().authenticated()
	                
	                // Protect all other endpoints
	        );

	        http.sessionManagement(session -> 
	                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        );

//	        http.httpBasic(withDefaults());

	        http.csrf(csrf -> csrf.disable());

	        http.headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));
	        
	        http.oauth2Login(oauth2 -> oauth2
	                .defaultSuccessUrl("/success", true)
	                .failureUrl("/failure")
	            );

	        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
	        
	        
	     // Add CORS configuration
	        http.cors(cors -> cors.configurationSource(request -> {
	            CorsConfiguration configuration = new CorsConfiguration();
	            configuration.setAllowedOrigins(List.of(frontendUrl,"http://localhost:5173")); // Add allowed origins
	            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
	            configuration.setAllowedHeaders(List.of("*"));
//	            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "*"));
	            configuration.setExposedHeaders(List.of("Authorization"));
	            configuration.setAllowCredentials(true); // Allow cookies
	            return configuration;
	        }));
	        
	        http.logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/login")
	                .clearAuthentication(true)
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID")
	                .permitAll()
	            );

	        return http.build();
	    }
	 
	 
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	     return authenticationConfiguration.getAuthenticationManager();
	 }

	    @Bean
	    public DataSource dataSource() {
//	        return new EmbeddedDatabaseBuilder()
//	                .setType(EmbeddedDatabaseType.H2)
//	                // Use default schema for JdbcUserDetailsManager
//	                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//	                .build();
//	    	
	    	
	    	return DataSourceBuilder.create()
	                .url("jdbc:postgresql://ep-weathered-credit-a1rhmr7b.ap-southeast-1.aws.neon.tech:5432/task-manager-spirng?sslmode=require")
	                .username("task-manager-spirng_owner")
	                .password("ypuQ6eqNEXI1")
	                .driverClassName("org.postgresql.Driver")
	                .build();
//	    	
	    }

	    @Bean
	    @Primary
	    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
	        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

	        // Default schema queries (you can customize these if needed)
	        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
	        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");

	        // Create default users (optional)
//	        var user = User.withUsername("in28minutes")
//	                .password(passwordEncoder.encode("dummy"))
//	                .roles("USER")
//	                .build();
//
//	        var admin = User.withUsername("admin")
//	                .password(passwordEncoder.encode("dummy"))
//	                .roles("ADMIN", "USER")
//	                .build();

	        // Check if users already exist before creating them
//	        if (!jdbcUserDetailsManager.userExists("in28minutes")) {
//	            jdbcUserDetailsManager.createUser(user);
//	        }
//	        if (!jdbcUserDetailsManager.userExists("admin")) {
//	            jdbcUserDetailsManager.createUser(admin);
//	        }

	        return jdbcUserDetailsManager;
	    }

	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public KeyPair keyPair() {
	        try {
	            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	            keyPairGenerator.initialize(2048);
	            return keyPairGenerator.generateKeyPair();
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	    }

	    @Bean
	    public RSAKey rsaKey(KeyPair keyPair) {
	        return new RSAKey
	                .Builder((RSAPublicKey) keyPair.getPublic())
	                .privateKey(keyPair.getPrivate())
	                .keyID(UUID.randomUUID().toString())
	                .build();
	    }

	    @Bean
	    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
	        var jwkSet = new JWKSet(rsaKey);

	        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
	    }

	    @Bean
	    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
	    	System.out.println("into the jwtDecoder");
	        return NimbusJwtDecoder
	                .withPublicKey(rsaKey.toRSAPublicKey())
	                .build();
	    }

	    @Bean
	    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
	        return new NimbusJwtEncoder(jwkSource);
	    }

}
