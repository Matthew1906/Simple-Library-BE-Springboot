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

    public void createMember(Member newMember) {
        if(this.repository.existsByCode(newMember.getCode())){
            throw new UniqueException("Member", "code", newMember.getCode());
        }
        this.repository.save(newMember);
    }

    public void updateMember(String id, Member updatedMember) {
        Member member = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Member", id));  
        
        if(this.repository.existsByCodeAndIdNot(updatedMember.getCode(), id)){
            throw new UniqueException("Member", "code", updatedMember.getCode());
        }

        member.setCode(updatedMember.getCode());
        member.setName(updatedMember.getName());
        member.setEmail(updatedMember.getEmail());
        member.setPhone(updatedMember.getPhone());
        this.repository.save(member);
    }

    public void deleteMember(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Member", id);
        }
        this.repository.deleteById(id);
    }
        
}
