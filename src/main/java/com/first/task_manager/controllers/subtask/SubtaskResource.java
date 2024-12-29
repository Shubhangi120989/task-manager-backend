package com.first.task_manager.controllers.subtask;

//import java.time.LocalDateTime;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
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
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.first.task_manager.repositories.TaskRepository;
import com.first.task_manager.repositories.SubtaskRepository;
import com.first.task_manager.repositories.TaskRepository;
import com.first.task_manager.schema.Subtask;
import com.first.task_manager.schema.Task;
//import com.first.task_manager.schema.Task;

import jakarta.persistence.EntityNotFoundException;

//@RestController
public class SubtaskResource {
//	@Autowired
//	private SubtaskRepository subtaskRepository;
//	@Autowired
//	private TaskRepository taskRepository;
//	
//	
//	@PostMapping("/subtasks/{taskId}")
//	public ResponseEntity<Subtask> createSubtask(@PathVariable Long taskId,@RequestParam String title,@RequestParam String description){
//         Optional<Task> task=taskRepository.findById(taskId);;
//		
//		if(task.isEmpty()) {
//			throw new EntityNotFoundException("Task not found");
//			
//		}
//		Subtask subtask=new Subtask();
//		subtask.setTitle(title);
//		subtask.setCompleted(false);
//		subtask.setDescription(description);
////		subtask.setTask(task.get());
//		
//		return ResponseEntity.created(null).body(subtaskRepository.save(subtask));
//		
//	}
//	
//	@GetMapping("/subtasks/{taskId}")
//	public ResponseEntity<List<Subtask>> getsubTasksOfATask(@PathVariable Long taskId){
//		 Optional<Task> task=taskRepository.findById(taskId);
//			
//			if(task.isEmpty()) {
//				throw new EntityNotFoundException("Task not found");
//				
//			}
//			
//			return ResponseEntity.ok().body(task.get().getSubtasks());
//	}
//	
//	@DeleteMapping("/subtasks/{subtaskId}")
//	public ResponseEntity<String> deleteSubtask(@PathVariable Long subtaskId){
//		try {
//			subtaskRepository.deleteById(subtaskId);
//		}catch (Exception e) {
//			return ((BodyBuilder) ResponseEntity.notFound()).body("subtask not found");
//		}
//		
//		return ResponseEntity.ok().body("Subtask deleted");
//	}
//	
//	@PatchMapping("/subtasks/{subtaskId}")
//	public ResponseEntity<Subtask> updateSubtask(@PathVariable Long subtaskId,@RequestParam String title,@RequestParam String description){
//		
//		Optional<Subtask> subtask=subtaskRepository.findById(subtaskId);;
//		
//		if(subtask.isEmpty()) {
//			throw new EntityNotFoundException("task not found");
//			
//		}
//		
//		subtask.get().setTitle(title);
//		subtask.get().setDescription(description);
//		
//		return ResponseEntity.ok().body(subtaskRepository.save(subtask.get()));
//	}
//	
//	
//	@GetMapping("/subtasks/get-by-id/{subtaskId}")
//	public ResponseEntity<Subtask> getSubtaskById(@PathVariable Long subtaskId){
//        Optional<Subtask> subtask=subtaskRepository.findById(subtaskId);
//		
//		if(subtask.isEmpty()) {
//			throw new EntityNotFoundException("subtask not found");
//			
//		}
//		
//		return ResponseEntity.ok().body(subtask.get());
//	}
//	
//	@PostMapping("/subtasks/{subtaskId}/toggleComplete")
//	public ResponseEntity<Subtask> toggleCompleted(@PathVariable Long subtaskId){
//         Optional<Subtask> subtask=subtaskRepository.findById(subtaskId);
//		
//		if(subtask.isEmpty()) {
//			throw new EntityNotFoundException("subtask not found");
//			
//		}
//		
//		if(subtask.get().isCompleted()==false) {
//			subtask.get().setCompleted(true);
//			subtask.get().setCompletedAt(LocalDateTime.now());
//		}else {
//			subtask.get().setCompleted(false);
//			subtask.get().setCompletedAt(null);
//		}
//		
//		return ResponseEntity.ok().body(subtaskRepository.save(subtask.get()));
//		
//		
//	}
//	

}
