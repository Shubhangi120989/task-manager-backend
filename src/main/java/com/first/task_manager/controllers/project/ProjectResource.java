package com.first.task_manager.controllers.project;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first.task_manager.repositories.ProjectRepository;
import com.first.task_manager.repositories.UserDetailsRepository;
import com.first.task_manager.schema.Project;
import com.first.task_manager.schema.Users;

//import jakarta.persistence.EntityNotFoundException;

@Controller
public class ProjectResource {
	@Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserDetailsRepository userDetailRepository;
    
    
//    @Bean
    private String getLoggedInUsername() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
    	 if (authentication instanceof OAuth2AuthenticationToken) {
		        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
		        
		        return (String) attributes.get("email");
        } 
        return authentication.getName(); 
    }
    
    
	@PostMapping("/projects")
	public ResponseEntity<Project> createProject(@RequestParam String title,@RequestParam String description){
	
		System.out.println("in the controllr");
//	String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//	Users user = userService.getLoggedInUser();
		String loggedInUsername=getLoggedInUsername();
	Optional<Users> user;
	try {
		user=userDetailRepository.findByUsername(loggedInUsername);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(null);
	}
	
	Project project= new Project();
	project.setUser(user.get());
    project.setTitle(title);
    project.setDescription(description);
    project.setCreatedAt(LocalDateTime.now());
//    projectRepository.save(project);
    
    return ResponseEntity.created(null).body(projectRepository.save(project));
	
		
	}
	
	@GetMapping("/projects")
	public ResponseEntity<List<Project>> getAllProjectsForLoggedInUser() {
//		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		String loggedInUsername=getLoggedInUsername();
		System.out.println("loggedInUsername" +loggedInUsername);
		
		Optional<Users> user=userDetailRepository.findByUsername(loggedInUsername);
		return ResponseEntity.ok().body(user.get().getProjects());
		
//		List<Project> projects = userDetails.getProjectsByUsername(loggedInUsername);
	}
	
	@DeleteMapping("/projects/{projectId}")
	public ResponseEntity<String> deleteProject(@PathVariable Long projectId){
		try {
			projectRepository.deleteById(projectId);
		}catch (Exception e) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("project not found");
		}
		
		return ResponseEntity.ok().body("Project deleted");
		
		
		
	}
	
	@PatchMapping("projects/{projectId}")
	public ResponseEntity<?> updateProject(@PathVariable Long projectId,@RequestParam String title,@RequestParam String description){
		Optional<Project> project=projectRepository.findById(projectId);;
//		try {
//			project=
//		}catch (Exception e) {
//			return ResponseEntity.badRequest().body(null);
//		}
		
		if(project.isEmpty()) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("project not found");
			
		}
		
		project.get().setTitle(title);
		project.get().setDescription(description);
		project.get().setUpdatedAt(LocalDateTime.now());
		return ResponseEntity.ok().body(projectRepository.save(project.get()));
	}
	
	@GetMapping("projects/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable Long projectId){
		Optional<Project> project=projectRepository.findById(projectId);
		
		if(project.isEmpty()) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("project not found");
			
		}
		
		return ResponseEntity.ok().body(project.get());
		
		
	}
	
	

}
