package com.learn.simple_library_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.learn.simple_library_be.model.Member;

public interface MemberRepository extends JpaRepository<Member, String>, JpaSpecificationExecutor<Member>{

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);

}
