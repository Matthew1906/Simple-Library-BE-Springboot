package com.learn.simple_library_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.learn.simple_library_be.error.NotFoundException;
import com.learn.simple_library_be.error.UniqueException;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.model.Member;
import com.learn.simple_library_be.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }
    
    public Overview<Member> getMemberOverview(Specification<Member> spec, Pageable pagination) {
        long memberCount = this.repository.count();
        Page<Member> members = this.repository.findAll(spec, pagination);
        return new Overview<>(memberCount, members.getContent()); 
    }

    public void createMember(Member body) {
        if(this.repository.existsByCode(body.getCode())){
            throw new UniqueException("Member", "code", body.getCode());
        }
        this.repository.save(body);
    }

    public void updateMember(String id, Member body) {
        Member member = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Member", id));  
        
        if(this.repository.existsByCodeAndIdNot(body.getCode(), id)){
            throw new UniqueException("Member", "code", body.getCode());
        }

        member.setCode(body.getCode());
        member.setName(body.getName());
        member.setEmail(body.getEmail());
        member.setPhone(body.getPhone());
        this.repository.save(member);
    }

    public void deleteMember(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Member", id);
        }
        this.repository.deleteById(id);
    }
        
}
