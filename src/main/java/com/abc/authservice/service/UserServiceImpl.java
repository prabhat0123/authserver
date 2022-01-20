package com.abc.authservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abc.authservice.domain.Role;
import com.abc.authservice.domain.User;
import com.abc.authservice.repositories.RoleRepo;
import com.abc.authservice.repositories.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
	public User saveUser(User user) {

		log.info("Saving new {} to Database", user.getName());
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {

		log.info("Saving new {} to Database", role);
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {

		log.info("Adding role {} to User {} ", username, roleName);

		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByRoleName(roleName);

		user.getRoles().add(role);

	}

	@Override
	public User getUser(String username) {

		log.info("Fetch User {} ", username);
		return userRepo.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {

		log.info("Fetch All user");
		return userRepo.findAll();
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}