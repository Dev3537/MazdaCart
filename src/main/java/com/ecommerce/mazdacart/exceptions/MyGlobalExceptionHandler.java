package com.ecommerce.mazdacart.exceptions;

import com.ecommerce.mazdacart.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import static com.ecommerce.mazdacart.util.EcomConstants.HANDLED_BY_GENERIC_EXCEPTION_HANDLER;

@RestControllerAdvice
public class MyGlobalExceptionHandler {


	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> myResourceNotFoundException (ResourceNotFoundException e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle("Resource Not Found");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(APIException.class)
	public ResponseEntity<ExceptionResponse> myAPIException (APIException e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle("API Exception");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> myGeneralException (Exception e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle(HANDLED_BY_GENERIC_EXCEPTION_HANDLER);
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> myMethodArgumentNotValidException (MethodArgumentNotValidException e) {
		ExceptionResponse response = new ExceptionResponse();
		e.getBindingResult().getAllErrors().forEach(err -> {
			String field = ((FieldError) err).getField();
			String message = err.getDefaultMessage();
			response.setTitle("Request Validation Failed");
			response.setMessage(field + " : " + message);

		});
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredsException.class)
	public ResponseEntity<ExceptionResponse> myBadCredsException (BadCredsException e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle("User UnAuthorized");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<ExceptionResponse> myRestClientException (RestClientException e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle("External rest client down");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<ExceptionResponse> myRestClientResponseException (RestClientResponseException e) {
		ExceptionResponse response = new ExceptionResponse();
		response.setTitle("External rest client exception");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, e.getStatusCode());
	}

}
