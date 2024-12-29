package com.first.task_manager.controllers.task;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.aayushatharva.brotli4j.encoder.Encoder;
//import com.first.task_manager.BrotliUtils;
import com.first.task_manager.repositories.ProjectRepository;
import com.first.task_manager.repositories.TaskRepository;
import com.first.task_manager.schema.Project;
import com.first.task_manager.schema.Task;

//import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


//record TaskRequest(String title,String description,LocalDateTime dueDate,Long priority,List<String> tags) {};

@RestController
public class TaskResource {
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	@PostMapping("/tasks/{projectId}")
	public ResponseEntity<?> createTask(@PathVariable Long projectId,@RequestBody Task task){
         Optional<Project> project=projectRepository.findById(projectId);;
		
		if(project.isEmpty()) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("Project not found");			
//			throw new EntityNotFoundException("Project not found");
			
		}
		task.setCreatedAt(LocalDateTime.now());
		task.setProject(project.get());
		task.setStatus(Task.Status.TO_DO);
		return ResponseEntity.created(null).body(taskRepository.save(task));
		
	}
	
	@GetMapping("/tasks/{projectId}")
	@Transactional
	public ResponseEntity<?> getTasksOfAProject(@PathVariable Long projectId){
		 Optional<Project> project=projectRepository.findById(projectId);;
			
			if(project.isEmpty()) {
				return ((BodyBuilder) ResponseEntity.notFound()).body("Task not found");	
//				throw new EntityNotFoundException("Project not found");
				
			}
			
			return ResponseEntity.ok().body(project.get().getTasks());
	}
	
	@DeleteMapping("/tasks/{taskId}")
	public ResponseEntity<String> deleteTask(@PathVariable Long taskId){
		try {
			taskRepository.deleteById(taskId);
		}catch (Exception e) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("task not found");
		}
		
		return ResponseEntity.ok().body("Task deleted");
	}
	
	@PatchMapping("tasks/{taskId}")
	public ResponseEntity<?> updateTask(
	        @PathVariable Long taskId, 
	        @RequestBody Task updatedTask) {
	    
	    // Find the task by ID
	    Optional<Task> existingTaskOptional = taskRepository.findById(taskId);

	    if (existingTaskOptional.isEmpty()) {
	    	return ((BodyBuilder) ResponseEntity.notFound()).body("Task not found");	
	    }

	    Task existingTask = existingTaskOptional.get();

	    // Update fields only if they are not null
	    if (updatedTask.getTitle() != null) {
	        existingTask.setTitle(updatedTask.getTitle());
	    }
	    if (updatedTask.getDescription() != null) {
	        existingTask.setDescription(updatedTask.getDescription());
	    }
	    if (updatedTask.getDueDate() != null) {
	        existingTask.setDueDate(updatedTask.getDueDate());
	    }
	    if (updatedTask.getTags() != null) {
	        existingTask.setTags(updatedTask.getTags());
	    }
	    if (updatedTask.getPriority() != null) {
	        existingTask.setPriority(updatedTask.getPriority());
	    }
	    if (updatedTask.getStatus() != null) {
	        existingTask.setStatus(updatedTask.getStatus());
	    }

	    // Update the `updatedAt` field
	    existingTask.setUpdatedAt(LocalDateTime.now());

	    // Save and return the updated task
	    return ResponseEntity.ok(taskRepository.save(existingTask));
	}
	
	
	
	@PatchMapping("tasks/{taskId}/status")
	public ResponseEntity<?> updateTaskStatus(
	        @PathVariable Long taskId,
	        @RequestBody String status) {
	    
	    // Find the task by ID
	    Optional<Task> existingTaskOptional = taskRepository.findById(taskId);

	    if (existingTaskOptional.isEmpty()) {
	    	return ((BodyBuilder) ResponseEntity.notFound()).body("Task not found");	
	    }

	    Task existingTask = existingTaskOptional.get();

	    // Validate the status value
	    try {
	        Task.Status updatedStatus = Task.Status.valueOf(status.toUpperCase());
	        existingTask.setStatus(updatedStatus);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Invalid status value. Allowed values: TO_DO, IN_PROGRESS, DONE");
	    }

	    // Update the `updatedAt` field
	    existingTask.setUpdatedAt(LocalDateTime.now());

	    // Save and return the updated task
	    return ResponseEntity.ok(taskRepository.save(existingTask));
	}

	
	
	@GetMapping("tasks/get-by-id/{taskId}")
	@Transactional
	public ResponseEntity<?> getTaskById(@PathVariable Long taskId){
        Optional<Task> task=taskRepository.findById(taskId);
		
		if(task.isEmpty()) {
			return ((BodyBuilder) ResponseEntity.notFound()).body("Task not found");	
			
		}
		
		return ResponseEntity.ok().body(task.get());
	}
//	@PostMapping("/{taskId}/saveBoard")
//    public ResponseEntity<String> saveBoardData(@PathVariable Long taskId, @RequestBody String boardData) {
//        try {
//            // Fetch the task from the database
//            Optional<Task> optionalTask = taskRepository.findById(taskId);
//            if (optionalTask.isEmpty()) {
//                return ResponseEntity.status(404).body("Task not found.");
//            }
//
//            Task task = optionalTask.get();
//
//            // Save the raw board data directly as BYTEA
//            task.setBoard_data(boardData.getBytes(StandardCharsets.UTF_8));
//            taskRepository.save(task);
//
//            return ResponseEntity.ok("Board data saved successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error saving board data: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/{taskId}/getBoard")
//    public ResponseEntity<String> getBoardData(@PathVariable Long taskId) {
//        try {
//            Optional<Task> optionalTask = taskRepository.findById(taskId);
//            if (optionalTask.isEmpty()) {
//                return ResponseEntity.status(404).body("Task not found.");
//            }
//
//            Task task = optionalTask.get();
//
//            // Retrieve and convert the BYTEA data back to String
//            byte[] boardDataBytes = task.getBoard_data();
//            String boardData = new String(boardDataBytes, StandardCharsets.UTF_8);
//
//            return ResponseEntity.ok().body(boardData);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error getting board data: " + e.getMessage());
//        }
//    }

	
	 @PostMapping("/tasks/{taskId}/saveBoard")
	    public ResponseEntity<String> saveBoardData(@PathVariable Long taskId, @RequestBody String boardData) {
//	        try {
//	            // Fetch the task from the database
//	        	System.out.println("boardData "+boardData);
//	            Optional<Task> optionalTask = taskRepository.findById(taskId);
//	            if (optionalTask.isEmpty()) {
//	                return ResponseEntity.status(404).body("Task not found.");
//	            }
//
//	            Task task = optionalTask.get();

//	            // Compress the board data using Brotli
//	            byte[] compressedData = Encoder.compress(boardData.getBytes());
//	            System.out.println("compressedData " + compressedData);
//
//	            // Encode the compressed data as Base64 to transmit as a string
//	            String compressedString = Base64.getEncoder().encodeToString(compressedData);
//
//	            // Save the compressed data to the task
//	            task.setBoard_data(compressedString.getBytes());
//
//	            taskRepository.save(task);
//
//	            return ResponseEntity.ok("Board data saved successfully!");
//	        } catch (Exception e) {
//	            return ResponseEntity.status(500).body("Error saving board data: " + e.getMessage());
//	        }
		 
		 
		 
		 try {
           // Fetch the task from the database
           Optional<Task> optionalTask = taskRepository.findById(taskId);
           if (optionalTask.isEmpty()) {
               return ResponseEntity.status(404).body("Task not found.");
           }

           Task task = optionalTask.get();

           // Save the raw board data directly as BYTEA
           task.setBoard_data(boardData.getBytes(StandardCharsets.UTF_8));
           taskRepository.save(task);

           return ResponseEntity.ok("Board data saved successfully!");
       } catch (Exception e) {
           return ResponseEntity.status(500).body("Error saving board data: " + e.getMessage());
       }
	        	
	        	        	
	    }
	 
	 @GetMapping("/tasks/{taskId}/getBoard")
	 public ResponseEntity<String> getBoardData(@PathVariable Long taskId){
//		 try {
//			 Optional<Task> optionalTask = taskRepository.findById(taskId);
//	            if (optionalTask.isEmpty()) {
//	                return ResponseEntity.status(404).body("Task not found.");
//	            }
//	            Task task=optionalTask.get();
//	            byte[] compressedData = task.getBoard_data();
//
//	            // Decode and decompress the data
//	            byte[] decompressedData = BrotliUtils.BrotliDecode(compressedData); // Use a library for Brotli decompression
//	            String decompressedString = new String(decompressedData);
//	            return ResponseEntity.ok().body(decompressedString);
//		 }catch (Exception e) {
//	            return ResponseEntity.status(500).body("Error getting board data: " + e.getMessage());
//	        }
		 
		 
		 
		 try {
           Optional<Task> optionalTask = taskRepository.findById(taskId);
           if (optionalTask.isEmpty()) {
               return ResponseEntity.status(404).body("Task not found.");
           }

           Task task = optionalTask.get();

           // Retrieve and convert the BYTEA data back to String
           byte[] boardDataBytes = task.getBoard_data();
           System.out.println(boardDataBytes);
           if(boardDataBytes==null) {
        	   return ResponseEntity.ok().body(null);
           }
           String boardData = new String(boardDataBytes, StandardCharsets.UTF_8);
           

           return ResponseEntity.ok().body(boardData);
       } catch (Exception e) {
           return ResponseEntity.status(500).body("Error getting board data: " + e.getMessage());
       }
		 
		 
		 
		 
	 }
	
	  

}

