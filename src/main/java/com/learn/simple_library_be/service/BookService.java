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

    public void createBook(BookMutation newBook) {
        if(this.repository.existsByTitle(newBook.getTitle())){
            throw new UniqueException("Book", "title", newBook.getTitle());
        }
        Author author = this.authorRepository.findById(newBook.getAuthor()).orElseThrow(() -> new NotFoundException("Author", newBook.getAuthor()));
        Book book = new Book(newBook.getTitle(), author, newBook.getPublishingYear(), newBook.getDescription(), newBook.getType());
        this.repository.save(book);
    }

    public void updateBook(String id, BookMutation updatedBook) {
        
        Book book = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Book", id));          
        
        if(this.repository.existsByTitleAndIdNot(updatedBook.getTitle(), id)){
            throw new UniqueException("Book", "title", updatedBook.getTitle());
        }
         Author author = this.authorRepository.findById(updatedBook.getAuthor()).orElseThrow(() -> new NotFoundException("Author", updatedBook.getAuthor()));

        book.setDescription(updatedBook.getDescription());
        book.setAuthor(author);
        book.setPublishingYear(updatedBook.getPublishingYear());
        book.setTitle(updatedBook.getTitle());
        book.setType(updatedBook.getType());

        this.repository.save(book);
    }

    public void deleteBook(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Book", id);
        }
        this.repository.deleteById(id);
    }
        
}
