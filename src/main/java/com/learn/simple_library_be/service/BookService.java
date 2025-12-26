package com.learn.simple_library_be.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.learn.simple_library_be.error.NotFoundException;
import com.learn.simple_library_be.error.UniqueException;
import com.learn.simple_library_be.mapping.BookMutation;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.model.Author;
import com.learn.simple_library_be.model.Book;
import com.learn.simple_library_be.repository.AuthorRepository;
import com.learn.simple_library_be.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository repository, AuthorRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    public Overview<Book> getBookOverview(Specification<Book> spec, Pageable pagination) {
        long bookCount = this.repository.count();
        Page<Book> books = this.repository.findAll(spec, pagination);
        return new Overview<>(bookCount, books.getContent());  
    }

    public void createBook(BookMutation body) {
        if(this.repository.existsByTitle(body.getTitle())){
            throw new UniqueException("Book", "title", body.getTitle());
        }
        Author author = this.authorRepository.findById(body.getAuthor()).orElseThrow(() -> new NotFoundException("Author", body.getAuthor()));
        Book book = new Book(body.getTitle(), author, body.getPublishingYear(), body.getDescription(), body.getType());
        this.repository.save(book);
    }

    public void updateBook(String id, BookMutation body) {
        
        Book book = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Book", id));          
        
        if(this.repository.existsByTitleAndIdNot(body.getTitle(), id)){
            throw new UniqueException("Book", "title", body.getTitle());
        }
         Author author = this.authorRepository.findById(body.getAuthor()).orElseThrow(() -> new NotFoundException("Author", body.getAuthor()));

        book.setDescription(body.getDescription());
        book.setAuthor(author);
        book.setPublishingYear(body.getPublishingYear());
        book.setTitle(body.getTitle());
        book.setType(body.getType());

        this.repository.save(book);
    }

    public void deleteBook(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Book", id);
        }
        this.repository.deleteById(id);
    }
        
}
