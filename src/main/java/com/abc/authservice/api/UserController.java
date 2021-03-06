package com.abc.authservice.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abc.authservice.domain.Role;
import com.abc.authservice.domain.User;
import com.abc.authservice.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class UserController {

	private final UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok().body(userService.getUsers());
	}

	@PostMapping("/user/save")
	public ResponseEntity<User> saveuser(@RequestBody User user) {

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}

	@PostMapping("/role/save")
	public ResponseEntity<Role> saveuser(@RequestBody Role role) {

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.ok().body(userService.saveRole(role));
	}

	@PostMapping("/roleToUser")
	public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm roleToUser) {

		userService.addRoleToUser(roleToUser.getUserName(), roleToUser.getRoleName());
		return ResponseEntity.ok().build();
	}
	


}

@Data
class RoleToUserForm {
	private String userName;
	private String roleName;
}