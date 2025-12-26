package com.learn.simple_library_be.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.simple_library_be.mapping.FilterSpecification;
import com.learn.simple_library_be.mapping.ModelResponse;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.mapping.Response;
import com.learn.simple_library_be.model.Author;
import com.learn.simple_library_be.service.AuthorService;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }
    
    @GetMapping
    public ModelResponse<Author> getAuthorOverview(
        @RequestParam(required=false) Integer page,
        @RequestParam(required=false) Integer numPages,
        @RequestParam(required=false) List<String> filter,
        @RequestParam(defaultValue = "id-asc") String sort
    ) {
        String[] sortValues = sort.split("-");
        Sort.Direction direction = sortValues[sortValues.length-1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Specification<Author> spec = new FilterSpecification<Author>().build(filter);

        if(page!=null && numPages != null){
            Pageable pageable = PageRequest.of(page-1, numPages, Sort.by(direction, sortValues[0]));
            Overview<Author> result = this.service.getAuthorOverview(spec, pageable);
            
            return new ModelResponse<>(
                true, 200, "Successfully retrieved authors", 
                result.getCount(), result.getData()
            );
        }

        Overview<Author> result = this.service.getAuthorOverview(spec,  Pageable.unpaged());
            
        return new ModelResponse<>(
            true, 200, "Successfully retrieved authors", 
            result.getCount(), result.getData()
        );
        
    }

    @PostMapping(consumes = "multipart/form-data")
    public Response createAuthor(@ModelAttribute Author body) {
        this.service.createAuthor(body);
        return new Response(true, 201, "Successfully created a new author");
    }

    @PutMapping(value="/{id}", consumes="multipart/form-data")
    public Response updateAuthor(@PathVariable String id, @ModelAttribute Author body) {
        this.service.updateAuthor(id, body);
        return new Response(true, 200, "Successfully updated author");
    }

    @DeleteMapping("/{id}")
    public Response deleteAuthor(@PathVariable String id){
        this.service.deleteAuthor(id);
        return new Response(true, 200, "Successfully deleted author");
    }
        
}
