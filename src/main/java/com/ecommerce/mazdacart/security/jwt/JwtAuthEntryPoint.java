package com.ecommerce.mazdacart.security.jwt;

import com.ecommerce.mazdacart.payload.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence (HttpServletRequest request, HttpServletResponse response,
	                      AuthenticationException authException) throws IOException, ServletException {

		log.error("Unauthorized error:{}", authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setTitle("Unauthorized");
		exceptionResponse.setMessage(authException.getMessage());

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), exceptionResponse);


	}
}
