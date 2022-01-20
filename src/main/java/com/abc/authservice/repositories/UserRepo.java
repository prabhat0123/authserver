package com.abc.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.authservice.domain.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	

}
