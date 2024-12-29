package com.first.task_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.task_manager.schema.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{

}
