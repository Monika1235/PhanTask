package com.phantask.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of(
	                    "error", "Unauthorized",
	                    "message", "Authentication required"
	            ));
	}
	
	@ExceptionHandler(AccountDeactivatedException.class)
	public ResponseEntity<Map<String, Object>> handleDeactivated(AccountDeactivatedException ex) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(Map.of("error", ex.getMessage(), "code", "ACCOUNT_DEACTIVATED"));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("error", "Invalid username or password"));
	}
	
	@ExceptionHandler(AttendanceAlreadyMarkedException.class)
    public ResponseEntity<Map<String, Object>> handleAttendanceAlreadyMarked(
            AttendanceAlreadyMarkedException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }
	
	@ExceptionHandler(AttendanceAlreadyCompletedException.class)
    public ResponseEntity<ApiErrorResponse> handleAttendanceAlreadyCompleted(
            AttendanceAlreadyCompletedException ex) {

        log.warn("Attendance error: {}", ex.getMessage());

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
	
}
