package com.learn.simple_library_be.error;

public class BorrowingException extends RuntimeException {

  public BorrowingException(String description) {
    super(String.format("Failed to borrow book: %s", description ));
  }  
}
