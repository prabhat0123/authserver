package com.abc.authservice.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.abc.authservice.util.JWTTokenUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationUtil {

	public static void setSecurityContext(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		try {

			String token = authorizationHeader.substring("Bearer ".length());

			String username = JWTTokenUtil.getSubject(token);
			String roles[] = JWTTokenUtil.getClaim("roles", token);

			Collection<SimpleGrantedAuthority> authorities = new ArrayList();
			Arrays.stream(roles).forEach(role -> {
				authorities.add(new SimpleGrantedAuthority(role));

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			});

		} catch (Exception ex) {

			log.error("Error Logging in {} ", ex.getLocalizedMessage());

			response.setHeader("error", ex.getLocalizedMessage());
			response.setStatus(HttpStatus.FORBIDDEN.value());
			Map<String, String> error = new HashMap<>();
			error.put("error_message", ex.getLocalizedMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			new ObjectMapper().writeValue(response.getOutputStream(), error);

		}

	}

}
