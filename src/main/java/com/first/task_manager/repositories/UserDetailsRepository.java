package com.first.task_manager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first.task_manager.schema.Users;

@Repository
public interface UserDetailsRepository extends JpaRepository<Users,String>{
	boolean existsByUsername(String username);

	Optional<Users> findByUsername(String loggedInUsername);

}
