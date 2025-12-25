package com.learn.simple_library_be.mapping;

public class Overview<T> {

    private final long count;
    private final Iterable<T> data;

    public Overview(long count, Iterable<T> data){
        this.count = count;
        this.data = data;
    }

    public long getCount(){ return this.count; }

    public Iterable<T> getData(){ return this.data; }
}
