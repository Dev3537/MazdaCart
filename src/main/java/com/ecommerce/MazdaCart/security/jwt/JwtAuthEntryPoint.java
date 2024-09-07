package com.ecommerce.MazdaCart.security.jwt;

import com.ecommerce.MazdaCart.payload.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

	@Override
	public void commence (HttpServletRequest request, HttpServletResponse response,
	                      AuthenticationException authException) throws IOException, ServletException {

		logger.error("Unauthorized error:{}", authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setTitle("Unauthorized");
		exceptionResponse.setMessage(authException.getMessage());

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), exceptionResponse);


	}
}
