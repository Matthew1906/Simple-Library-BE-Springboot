package com.learn.simple_library_be.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.simple_library_be.mapping.Response;

@RestControllerAdvice
class BorrowingAdvice {

  @ExceptionHandler(BorrowingException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
  public Response BorrowingHandler(BorrowingException ex) {
    return new Response(false, 422, ex.getMessage());
  }
}

