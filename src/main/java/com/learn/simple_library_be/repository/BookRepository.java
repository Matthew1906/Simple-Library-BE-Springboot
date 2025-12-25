package com.learn.simple_library_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.learn.simple_library_be.model.Book;

public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book>{

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, String id);

}
