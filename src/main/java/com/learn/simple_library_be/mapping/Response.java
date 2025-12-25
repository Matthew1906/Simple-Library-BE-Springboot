package com.learn.simple_library_be.mapping;

public class Response {

    private final int code;
    private final boolean status;
    private final String message;


    public Response(boolean status, int code, String message){
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() { return this.code; }
    public boolean isStatus() { return this.status; }
    public String getMessage() { return this.message; }

}