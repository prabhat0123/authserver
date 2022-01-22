package com.abc.authservice.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.abc.authservice.domain.Role;
import com.abc.authservice.domain.User;
import com.abc.authservice.service.UserService;
import com.abc.authservice.util.JWTTokenUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final UserService userService;

	@GetMapping("/app/abc")
	public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes) {

		// TO DO business logic to store TMN Token to Redis and return key

		return new RedirectView("https://release-portal.truemoney.com?key=radom_key");

	}

	@GetMapping("/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {

				String refresh_token = authorizationHeader.substring("Bearer ".length());
				
				String username =JWTTokenUtil.getSubject(refresh_token);
				User user = userService.getUser(username);
				List<String> roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
				String access_token =JWTTokenUtil.accessToken(user.getName(), roles);

				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (Exception ex) {

				log.error("Error Logging in {} ", ex.getLocalizedMessage());

				response.setHeader("error", ex.getLocalizedMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", ex.getLocalizedMessage());

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				new ObjectMapper().writeValue(response.getOutputStream(), error);

			}
		} else {

			new RuntimeException("Refresh token not found");

		}

	}
	

}
