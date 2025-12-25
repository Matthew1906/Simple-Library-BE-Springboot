package com.learn.simple_library_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.learn.simple_library_be.error.NotFoundException;
import com.learn.simple_library_be.error.UniqueException;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.model.Author;
import com.learn.simple_library_be.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }
    
    public Overview<Author> getAuthorOverview(Specification<Author> spec, Pageable pagination) {
        long authorCount = this.repository.count();
        Page<Author> authors = this.repository.findAll(spec, pagination);
        return new Overview<>(authorCount, authors.getContent()); 
    }

    public void createAuthor(Author newAuthor) {
        if(this.repository.existsByName(newAuthor.getName())){
            throw new UniqueException("Author", "name", newAuthor.getName());
        }
        this.repository.save(newAuthor);
    }

    public void updateAuthor(String id, Author updatedAuthor) {
        Author author = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Author", id));          
        if(this.repository.existsByNameAndIdNot(updatedAuthor.getName(), id)){
            throw new UniqueException("Author", "name", updatedAuthor.getName());
        }
        author.setName(updatedAuthor.getName());
        this.repository.save(author);
    }

    public void deleteAuthor(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Author", id);
        }
        this.repository.deleteById(id);
    }
        
}
