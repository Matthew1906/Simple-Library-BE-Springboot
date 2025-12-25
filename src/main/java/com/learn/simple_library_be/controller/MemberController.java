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
import com.learn.simple_library_be.model.Member;
import com.learn.simple_library_be.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }
    
    @GetMapping
    public ModelResponse<Member> getMemberOverview(
        @RequestParam(required=false) Integer page,
        @RequestParam(required=false) Integer numPages,
        @RequestParam(required=false) List<String> filter,
        @RequestParam(defaultValue = "id-asc") String sort
    ) {
        String[] sortValues = sort.split("-");
        Sort.Direction direction = sortValues[sortValues.length-1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Specification<Member> spec = new FilterSpecification<Member>().build(filter);

        if(page!=null && numPages != null){
            Pageable pageable = PageRequest.of(page-1, numPages, Sort.by(direction, sortValues[0]));
            Overview<Member> result = this.service.getMemberOverview(spec, pageable);
            
            return new ModelResponse<>(
                true, 200, "Successfully retrieved members", 
                result.getCount(), result.getData()
            );
        }

        Overview<Member> result = this.service.getMemberOverview(spec, Pageable.unpaged());
            
        return new ModelResponse<>(
            true, 200, "Successfully retrieved members", 
            result.getCount(), result.getData()
        );
    }

    @PostMapping
    public Response createMember(@ModelAttribute Member newMember) {
        this.service.createMember(newMember);
        return new Response(true, 201, "Successfully created a new member");
    }

    @PutMapping("/{id}")
    public Response updateMember(@PathVariable String id, @ModelAttribute Member updatedMember) {
        this.service.updateMember(id, updatedMember);
        return new Response(true, 200, "Successfully updated member");
    }

    @DeleteMapping("/{id}")
    public Response deleteMember(@PathVariable String id){
        this.service.deleteMember(id);
        return new Response(true, 200, "Successfully deleted member");
    }
        
}
