package com.learn.simple_library_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.learn.simple_library_be.model.Author;

public interface AuthorRepository extends JpaRepository<Author, String>, JpaSpecificationExecutor<Author>{

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);

}
