package com.abc.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.authservice.domain.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{
	
	Role findByRoleName(String roleName);
	

}
