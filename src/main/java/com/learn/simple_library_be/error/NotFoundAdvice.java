package com.learn.simple_library_be.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.simple_library_be.mapping.Response;

@RestControllerAdvice
class NotFoundAdvice {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Response NotFoundHandler(NotFoundException ex) {
    return new Response(false, 404, ex.getMessage());
  }
}

