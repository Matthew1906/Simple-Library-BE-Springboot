package com.learn.simple_library_be.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="author")
public class Author {
    
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique= true)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    protected Author() { }

    public Author(String name){ 
        this.name = name; 
    }

    @Override
    public String toString(){
        return String.format("Author  [id:%s, name:%s]", this.id, this.name);
    }

    public String getId(){ return this.id; }

    public String getName(){ return this.name; }

    public List<Book> getBooks(){ return this.books; }

    public void setName(String name){ this.name = name; }

}