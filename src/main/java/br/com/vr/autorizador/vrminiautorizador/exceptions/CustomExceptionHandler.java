package br.com.vr.autorizador.vrminiautorizador.exceptions;



import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final String KEY_USER_ERROR_NOT_FOUND = "0000";
	private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	private static final String BAD_REQUEST = "BAD_REQUEST";
	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String UNAUTHORIZED = "UNAUTHORIZED";
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
		log.error("Error", ex);
		ErrorResponse error = new ErrorResponse(INTERNAL_SERVER_ERROR, Collections.singletonList(ex.getLocalizedMessage()));
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	private ErrorResponse convertErrorMessage(String message) {
		ErrorResponse error;
		try {
			error = mapper.readValue(message, ErrorResponse.class);
		} catch (Exception e) {
			logger.error("Error mapping value from body in handling", e);
			error = new ErrorResponse(BAD_REQUEST, Collections.singletonList("It was not possible to identify the error."));
		}
		return error;
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return new ResponseEntity<>(
				new ErrorResponse(BAD_REQUEST,
						ex.getBindingResult().getFieldErrors().stream()
								.map(field -> String.format("%s - %s", field.getField(), field.getDefaultMessage())).collect(Collectors.toList())),
				status);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ErrorResponse error = new ErrorResponse(BAD_REQUEST, Collections.singletonList(ex.getLocalizedMessage()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public final ResponseEntity<ErrorResponse> handleClientErrorException(HttpClientErrorException ex, WebRequest request) {
		return new ResponseEntity<>(convertErrorMessage(ex.getResponseBodyAsString()), ex.getStatusCode());
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public final ResponseEntity<ErrorResponse> handleServerErrorException(HttpServerErrorException ex, WebRequest request) {
		return convertMessageToEspecialConditionInHttpServerErrorException(ex);
	}
	
	private ResponseEntity<ErrorResponse> convertMessageToEspecialConditionInHttpServerErrorException(HttpServerErrorException ex) {
		HttpStatus statusCode = ex.getStatusCode();
		ErrorResponse error;
		if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
			error = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.name(), Collections.singletonList(ex.getMessage()));
		} else {
			error = convertErrorMessage(ex.getResponseBodyAsString());
			if (error.getDetails().stream().anyMatch(erro -> erro.contains(KEY_USER_ERROR_NOT_FOUND))) {
				error = new ErrorResponse(BAD_REQUEST, Collections.singletonList("user not found."));
				statusCode = HttpStatus.BAD_REQUEST;
			}
		}
		return new ResponseEntity<>(error, statusCode);
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
		ErrorResponse error = new ErrorResponse(NOT_FOUND, Collections.singletonList(ex.getLocalizedMessage()));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse(BAD_REQUEST, Collections.singletonList(ex.getMostSpecificCause().getMessage()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
		ErrorResponse error = new ErrorResponse(BAD_REQUEST, Collections.singletonList(ex.getLocalizedMessage()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotAuthorizedException.class)
	public final ResponseEntity<ErrorResponse> handleGenericException(NotAuthorizedException ex, WebRequest request) {
		ErrorResponse error = new ErrorResponse(UNAUTHORIZED, Collections.singletonList(ex.getLocalizedMessage()));
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
}
