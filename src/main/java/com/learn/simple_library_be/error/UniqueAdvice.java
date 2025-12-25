package com.learn.simple_library_be.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.simple_library_be.mapping.Response;

@RestControllerAdvice
class UniqueAdvice {

  @ExceptionHandler(UniqueException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Response UniqueHandler(UniqueException ex) {
    return new Response(false, 409, ex.getMessage());
  }
}

