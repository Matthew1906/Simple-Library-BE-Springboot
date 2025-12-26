package com.learn.simple_library_be.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="borrowing")
public class Borrowing {
    
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties("borrowings")
    private Member member;

    @Column(name = "borrow_date", nullable = false)
    private Date borrowDate;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name="return_date")
    private Date returnDate;

    protected Borrowing() { }

    public Borrowing(Book book, Member member, Date borrow_date, Date due_date){
        this.book = book;
        this.member = member;
        this.borrowDate = borrow_date;
        this.dueDate = due_date;
    }

    public String getId(){ return this.id; }

    public Book getBook(){ return this.book; }

    public Member getMember(){ return this.member; }

    public Date getBorrowDate() { return this.borrowDate; }
    
    public Date getDueDate(){ return this.dueDate; }

    public Date getReturnDate(){ return this.returnDate; }

    public void setBook(Book book){ this.book = book; }

    public void setMember(Member member){ this.member = member; }

    public void setBorrowDate(Date date){ this.borrowDate = date; }

    public void setDueDate(Date date){ this.dueDate = date; }

    public void setReturnDate(Date date){ this.returnDate = date; }

}
