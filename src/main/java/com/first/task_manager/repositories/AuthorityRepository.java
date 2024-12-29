package com.first.task_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first.task_manager.schema.Authorities;

public interface AuthorityRepository extends JpaRepository<Authorities, Long>{

}
