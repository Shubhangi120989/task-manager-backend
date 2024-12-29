package com.first.task_manager.controllers.user;

import org.springframework.web.bind.annotation.RequestMapping;

//JdbcUserDetailsManager
import org.springframework.web.bind.annotation.RestController;

import com.first.task_manager.repositories.UserDetailsRepository;
import com.first.task_manager.schema.Users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import com.first.task_manager.security.JwtSecurityConfiguration.jdbcUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/register")
public class UserRegistrationController {
	private final JdbcUserDetailsManager userDetailsManager;
    private final UserDetailsRepository userDetailsRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
//    private Cloudinary cloudinary; // Injecting the Cloudinary bean

    public UserRegistrationController(JdbcUserDetailsManager userDetailsManager,
                                      UserDetailsRepository userDetailsRepository,
                                      BCryptPasswordEncoder passwordEncoder) {
    	super();
        this.userDetailsManager = userDetailsManager;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(
//    		@RequestParam("avatar") MultipartFile file, 
    		@RequestParam("avatar") String avatarUrl,
            @RequestParam("username") String username, 
            @RequestParam("name") String name, 
            @RequestParam("bio") String bio,
            @RequestParam("password") String password) {
    	
    	
    	
    	System.out.println("in the registration controller");
        // Check if the user already exists
        if (userDetailsManager.userExists(username)) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }
        
        
        //check if valid email
        if(!isValidEmail(username)) {
        	return ResponseEntity.badRequest().body("Invalid email/username");
        }
        
//        String avatarUrl;
//        try {
//        	var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
//        	avatarUrl = uploadResult.get("url").toString(); 
//        }catch(Exception e) {
//        	e.printStackTrace();
//        	return ResponseEntity.status(500).body("Error uploading file to Cloudinary: " + e.getMessage()); 
//        }
       

//        // Save user in the default 'users' table
        var user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER") // Default role
                .build();
        userDetailsManager.createUser(user);
        Optional<Users> userSaved=userDetailsRepository.findByUsername(username);
        userSaved.get().setBio(bio);
        userSaved.get().setName(name);
        userSaved.get().setAvatarUrl(avatarUrl);
//        userSaved.se

        
        
        userDetailsRepository.save(userSaved.get());

        return ResponseEntity.ok("User registered successfully.");
    } 
    
    public static boolean isValidEmail(String email) {
    	String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,7}$";
        final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    	
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    

}


