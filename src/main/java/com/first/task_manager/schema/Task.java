package com.first.task_manager.schema;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.first.task_manager.Base64Serializer;

@Entity
public class Task {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long taskId;

	    @ManyToOne
	    @JoinColumn(name = "projectId", nullable = false)
	    private Project project;

	    @Column(nullable = false)
	    private String title;

	    private String description;
	    private LocalDate dueDate;

	    @Enumerated(EnumType.STRING)
	    private Priority priority;

	    @Enumerated(EnumType.STRING)
	    private Status status;

	    @ElementCollection
	    private List<String> tags;
	    
//	    @Lob
	    @Basic(fetch = FetchType.EAGER)
//	    @JsonSerialize(using = Base64Serializer.class)
	    @JsonIgnore
	    @Column(columnDefinition = "BYTEA")
	    private byte[] board_data;

	    public byte[] getBoard_data() {
			return board_data;
		}



		public void setBoard_data(byte[] board_data) {
			this.board_data = board_data;
		}



		private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

	    // Enum for Priority
	    public enum Priority {
	        LOW, MEDIUM, HIGH, URGENT
	    }

	    // Enum for Status
	    public enum Status {
	        TO_DO, IN_PROGRESS, DONE
	    }

    
    
    public Task() {
    	
    }






	public Task(Long taskId, Project project, String title, String description, LocalDate dueDate,
			Priority priority, Status status, List<String> tags, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.taskId = taskId;
		this.project = project;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.status = status;
		this.tags = tags;
//		this.board_data = board_data;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}



	public Long getTaskId() {
		return taskId;
	}



	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}



	public Project getProject() {
		return project;
	}



	public void setProject(Project project) {
		this.project = project;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public LocalDate getDueDate() {
		return dueDate;
	}



	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}



	public Priority getPriority() {
		return priority;
	}



	public void setPriority(Priority priority) {
		this.priority = priority;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public List<String> getTags() {
		return tags;
	}



	public void setTags(List<String> tags) {
		this.tags = tags;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}



	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    

	

  
}
