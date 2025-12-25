package com.learn.simple_library_be.mapping;

public class BookMutation {
    private final String title, publishing_year, description, author_id;
    private final BookType type;

    public BookMutation(String title, String author_id, String publishing_year, String description, BookType type){
        this.title = title;
        this.author_id = author_id;
        this.publishing_year = publishing_year;
        this.description = description;
        this.type = type;
    }

    public String getTitle(){ return this.title; }

    public String getAuthor(){ return this.author_id; }

    public String getPublishingYear(){ return this.publishing_year; }

    public String getDescription(){ return this.description; }

    public BookType getType(){ return this.type; }
    
}
