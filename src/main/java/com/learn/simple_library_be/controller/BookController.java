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

import com.learn.simple_library_be.mapping.BookMutation;
import com.learn.simple_library_be.mapping.FilterSpecification;
import com.learn.simple_library_be.mapping.ModelResponse;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.mapping.Response;
import com.learn.simple_library_be.model.Book;
import com.learn.simple_library_be.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }
    
    @GetMapping
    public ModelResponse<Book> getBookOverview(
        @RequestParam(required=false) Integer page,
        @RequestParam(required=false) Integer numPages,
        @RequestParam(required=false) List<String> filter,
        @RequestParam(defaultValue = "id-asc") String sort
    ) {
        String[] sortValues = sort.split("-");
        
        Sort.Direction direction = sortValues[sortValues.length-1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Specification<Book> spec = new FilterSpecification<Book>().build(filter);

        if(page!=null && numPages != null){
            Pageable pageable = PageRequest.of(page-1, numPages, Sort.by(direction, sortValues[0]));
            Overview<Book> books = this.service.getBookOverview(spec, pageable);
            
            return new ModelResponse<>(
                true, 200, "Successfully retrieved books", 
                books.getCount(), books.getData()
            );
        }

        Overview<Book> books = this.service.getBookOverview(spec, Pageable.unpaged());
            
        return new ModelResponse<>(
            true, 200, "Successfully retrieved books", 
            books.getCount(), books.getData()
        );
    }

    @PostMapping(consumes = "multipart/form-data")
    public Response createBook(@ModelAttribute BookMutation body) {
        this.service.createBook(body);
        return new Response(true, 201, "Successfully created a new title");
    }

    @PutMapping(value="/{id}", consumes="multipart/form-data")
    public Response updateBook(@PathVariable String id, @ModelAttribute BookMutation body) {
        this.service.updateBook(id, body);
        return new Response(true, 200, "Successfully updated book");
    }

    @DeleteMapping("/{id}")
    public Response deleteBook(@PathVariable String id){
        this.service.deleteBook(id);
        return new Response(true, 200, "Successfully deleted book");
    }
        
}
