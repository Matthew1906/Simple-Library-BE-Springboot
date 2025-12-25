package com.learn.simple_library_be.mapping;

public class SingleResponse<T> extends Response {
    
    private final T data;

    public SingleResponse(boolean status, int code, String message, T data){
        super(status, code, message);
        this.data = data;
    }

    public T getData(){ return this.data; }

}
