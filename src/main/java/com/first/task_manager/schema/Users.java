package com.first.task_manager.schema;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class Users {
	
	
	@Id
    @Column(name = "username")
    private String username; // This will be the email

  
    private String name;
    
    


    @JsonIgnore
    private String password;

    private String avatarUrl;
    
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Project> projects;

    @Column(length = 500)
    private String bio;
    
    @Column(nullable=false)
    @JsonIgnore
    private Boolean enabled;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Authorities> authorities;

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
	
	
	public Boolean getEnabled() {
		return enabled;
	}

	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Users(String name, String username, String avatarUrl, String bio,List<Project> projects) {
		super();
//		this.id = id;
		this.name = name;
		this.username = username;
		this.avatarUrl = avatarUrl;
		this.bio = bio;
		this.projects=projects;
	}
	
	public List<Project> getProjects() {
		return projects;
	}
	
	
     
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Users(String name, String username, String avatarUrl, String bio) {
		
		this.name = name;
		this.username = username;
		this.avatarUrl = avatarUrl;
		this.bio = bio;
	}

	public Users() {
		
	}

	public void setPassword(String encode) {
		// TODO Auto-generated method stub
		
		
	}
    
    
}

