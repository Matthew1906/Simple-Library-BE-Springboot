package com.learn.simple_library_be.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.simple_library_be.mapping.BorrowingMutation;
import com.learn.simple_library_be.mapping.FilterSpecification;
import com.learn.simple_library_be.mapping.ModelResponse;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.mapping.Response;
import com.learn.simple_library_be.mapping.ReturnBorrowingMutation;
import com.learn.simple_library_be.model.Borrowing;
import com.learn.simple_library_be.service.BorrowingService;

@RestController
@RequestMapping("/borrowings")
public class BorrowingController {

    private final BorrowingService service;

    public BorrowingController(BorrowingService service) {
        this.service = service;
    }
    
    @GetMapping
    public ModelResponse<Borrowing> getBorrowingOverview(
        @RequestParam(required=false) Integer page,
        @RequestParam(required=false) Integer numPages,
        @RequestParam(required=false) List<String> filter,
        @RequestParam(defaultValue = "id-asc") String sort
    ) {
        String[] sortValues = sort.split("-");
        
        Sort.Direction direction = sortValues[sortValues.length-1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Specification<Borrowing> spec = new FilterSpecification<Borrowing>().build(filter);

        if(page!=null && numPages != null){
            Pageable pageable = PageRequest.of(page-1, numPages, Sort.by(direction, sortValues[0]));
            Overview<Borrowing> borrowings = this.service.getBorrowingOverview(spec, pageable);
            
            return new ModelResponse<>(
                true, 200, "Successfully retrieved borrowings", 
                borrowings.getCount(), borrowings.getData()
            );
        }

        Overview<Borrowing> borrowings = this.service.getBorrowingOverview(spec, Pageable.unpaged());
            
        return new ModelResponse<>(
            true, 200, "Successfully retrieved borrowings", 
            borrowings.getCount(), borrowings.getData()
        );
    }

    @PostMapping(consumes = "multipart/form-data")
    public Response createBorrowing(@ModelAttribute BorrowingMutation body) {
        this.service.createBorrowing(body);
        return new Response(true, 201, "Successfully created new borrowings");
    }

    @PatchMapping(consumes="multipart/form-data")
    public Response returnBorrowing(@ModelAttribute ReturnBorrowingMutation body) {
        this.service.returnBorrowing(body);
        return new Response(true, 200, "Successfully return books");
    }

    @DeleteMapping("/{id}")
    public Response deleteBorrowing(@PathVariable String id){
        this.service.deleteBorrowing(id);
        return new Response(true, 200, "Successfully deleted borrowing");
    }
        
}
