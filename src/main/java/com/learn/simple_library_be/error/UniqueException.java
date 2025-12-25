package com.learn.simple_library_be.error;

public class UniqueException extends RuntimeException {

  public UniqueException(String model, String identifier, String value){
    super(String.format("%s with this %s [%s] already exists", model, identifier, value));
  }  
}
 
