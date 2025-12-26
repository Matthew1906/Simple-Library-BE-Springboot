package com.learn.simple_library_be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.learn.simple_library_be.error.BorrowingException;
import com.learn.simple_library_be.error.NotFoundException;
import com.learn.simple_library_be.mapping.BorrowingMutation;
import com.learn.simple_library_be.mapping.Overview;
import com.learn.simple_library_be.mapping.ReturnBorrowingMutation;
import com.learn.simple_library_be.model.Book;
import com.learn.simple_library_be.model.Borrowing;
import com.learn.simple_library_be.model.Member;
import com.learn.simple_library_be.repository.BookRepository;
import com.learn.simple_library_be.repository.BorrowingRepository;
import com.learn.simple_library_be.repository.MemberRepository;

@Service
public class BorrowingService {

    @Value("${library.max.borrow}")
    private int maxBooksPerMember;

    private final BorrowingRepository repository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowingService(BorrowingRepository repository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public Overview<Borrowing> getBorrowingOverview(Specification<Borrowing> spec, Pageable pagination) {
        long bookCount = this.repository.count();
        Page<Borrowing> books = this.repository.findAll(spec, pagination);
        return new Overview<>(bookCount, books.getContent());  
    }

    public void createBorrowing(BorrowingMutation body) {
        // Get member
        Member member = this.memberRepository.findById(body.getMember()).orElseThrow(() -> new NotFoundException("Member", body.getMember()));
        if(body.getBooks().length == 0){
            throw new BorrowingException("must be borrowing at least 1 book");
        }
        // Validate if member can borrow that much books;
        long currentlyBorrowedBooks = this.repository.countByMemberAndReturnDateIsNull(member);
        if(currentlyBorrowedBooks + body.getBooks().length > this.maxBooksPerMember){
            throw new BorrowingException(String.format("this member can't have more than %d active borrowings", this.maxBooksPerMember));
        }
        // Loop through each book and make sure that the book is not currently borrowed
        for (String bookId : body.getBooks()) {
            Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book", bookId));
            if(this.repository.existsByBookAndReturnDateIsNull(book)){
                throw new BorrowingException(String.format("this book (%s) is currently being borrowed!", book.getTitle()));
            }
        }
        // Create an array list of borrowing
        List<Borrowing> borrowings = new ArrayList<>();
        // Loop through each book and create new borrowings
        for (String bookId: body.getBooks()){
            Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book", bookId));
            Borrowing borrowing = new Borrowing(book, member, body.getBorrowDate(), body.getDueDate());
            borrowings.add(borrowing);
        }
        // Save borrowings
        this.repository.saveAll(borrowings);
    }

    public void returnBorrowing(ReturnBorrowingMutation body) {
        // Loop through each borrowings and 
        for (String borrowingId : body.getBorrowings()) {
            Borrowing borrowing = this.repository.findById(borrowingId).orElseThrow(() -> new NotFoundException("Borrowing", borrowingId));
            Date returnDate = borrowing.getReturnDate();
            if( returnDate != null){
                throw new BorrowingException(
                    String.format("this book (%s) borrowed by (%s) is already returned!", 
                        borrowing.getBook().getTitle(),  borrowing.getMember().getName()
                    )
                );
            }
            borrowing.setReturnDate(body.getReturnDate());
            this.repository.save(borrowing);
        }
    }

    public void deleteBorrowing(String id){
        if(!this.repository.existsById(id)){
            throw new NotFoundException("Borrowing", id);
        }
        this.repository.deleteById(id);
    }
        
}
