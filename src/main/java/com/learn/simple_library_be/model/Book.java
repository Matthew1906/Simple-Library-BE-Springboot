package com.learn.simple_library_be.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learn.simple_library_be.mapping.BookType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="book")
public class Book {
    
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique= true)
    private String title;

    @Column(name = "publishing_year", nullable=false)
    private String publishingYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(length=500)
    private String description;

    @Column(nullable = false)
    private BookType type;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Borrowing> borrowings;

    protected Book() { }

    public Book(String title, Author author, String publishing_year, String description, BookType type){
        this.title = title;
        this.author = author;
        this.publishingYear = publishing_year;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString(){
        return String.format("Book  [id:%s, title:%s by %s]", this.id, this.title, this.author.getName());
    }

    public String getId(){ return this.id; }

    public String getTitle(){ return this.title; }

    public Author getAuthor(){ return this.author; }

    public String getPublishingYear(){ return this.publishingYear; }

    public String getDescription(){ return this.description; }

    public BookType getType(){ return this.type; }

    public List<Borrowing> getBorrowings(){ return this.borrowings; }

    public void setTitle(String title){ this.title = title; }

    public void setAuthor(Author author){ this.author = author; }

    public void setPublishingYear(String publishing_year){ this.publishingYear = publishing_year; }

    public void setDescription(String description){ this.description = description; }

    public void setType(BookType type){ this.type = type; }

}