package com.abc.authservice.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals("/auth/token") || request.getServletPath().equals("/auth/refresh")) {

			filterChain.doFilter(request, response);
		} /*else if (request.getServletPath().equals("/auth/app/abc")) {
			String tmnHeader = request.getHeader("tmn_access_token");
			// TO DO add logic to check header of TMN
			filterChain.doFilter(request, response);

		}*/ else {
			AuthorizationUtil.setSecurityContext(request, response);
			filterChain.doFilter(request, response);
		}

	}

}
