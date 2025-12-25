package com.learn.simple_library_be.mapping;

public class ModelResponse<T> extends Response {
    
    private final long count;
    private final Iterable<T> data;

    public ModelResponse(boolean status, int code, String message, long count, Iterable<T> data){
        super(status, code, message);
        this.count = count;
        this.data = data;
    }

    public long getCount(){ return this.count; }

    public Iterable<T> getData(){ return this.data; }

}
