package com.course2.apisecurity.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private static final ApiErrorMessage  GENERIC_ERROR_MESSAGE = new ApiErrorMessage("Sorry, some error happened");
    private static final ApiErrorMessage  INVALID_ERROR_MESSAGE = new ApiErrorMessage("Sorry, input is invalid");
    private static final ApiErrorMessage  CONSTRAINT_VIOLATION_MESSAGE = new ApiErrorMessage("Sorry, constraint violation happened");

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiErrorMessage> handleSQLException(SQLException e){
        LOG.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE).body(GENERIC_ERROR_MESSAGE);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        LOG.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE).body(INVALID_ERROR_MESSAGE);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorMessage> handleConstraintViolationException(ConstraintViolationException e){
        LOG.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE).body(CONSTRAINT_VIOLATION_MESSAGE);

    }
}
