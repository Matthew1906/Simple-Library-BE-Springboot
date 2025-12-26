package com.learn.simple_library_be.mapping;

import java.util.Date;

public class BorrowingMutation {
  
    private final String member_id;
    private final String[] books;
    private final Date borrow_date, due_date;

    public BorrowingMutation(String[] books, String member_id, Date borrow_date, Date due_date){
        this.books = books;
        this.member_id = member_id;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
    }

    public String[] getBooks(){ return this.books; }

    public String getMember(){ return this.member_id; }

    public Date getBorrowDate() { return this.borrow_date; }
    
    public Date getDueDate(){ return this.due_date; }

}
