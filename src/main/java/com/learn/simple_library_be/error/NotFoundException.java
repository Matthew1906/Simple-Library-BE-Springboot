package com.learn.simple_library_be.error;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String model, String id) {
    super(String.format("Could not find %s with identifier %s", model, id ));
  }  
}
