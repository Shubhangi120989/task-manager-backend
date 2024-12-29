package com.first.task_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first.task_manager.schema.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>{

}
