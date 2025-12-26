package com.learn.simple_library_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.learn.simple_library_be.model.Book;
import com.learn.simple_library_be.model.Borrowing;
import com.learn.simple_library_be.model.Member;

public interface BorrowingRepository extends JpaRepository<Borrowing, String>, JpaSpecificationExecutor<Borrowing>{

    boolean existsByBookAndReturnDateIsNull(Book book);

    long countByMemberAndReturnDateIsNull(Member member);

}
