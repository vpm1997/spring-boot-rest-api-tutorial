package com.staxrt.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * The type Global exception handler.
 *
 * @author Givantha Kalansuriya
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Resource not found exception response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public Mono<ResponseEntity<?>> resourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    ErrorResponse errorDetails =
        new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage(), request.getDescription(false));
    return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND));
  }

  /**
   * Global exception handler response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<?>> globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorResponse errorDetails =
        new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), request.getDescription(false));
    return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR));
  }
}