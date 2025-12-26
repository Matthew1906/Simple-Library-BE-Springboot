package com.learn.simple_library_be.mapping;

import java.util.Date;

public class ReturnBorrowingMutation {
;
    private final String[] borrowings;
    private final Date return_date;

    public ReturnBorrowingMutation(String[] borrowings, Date return_date){
        this.borrowings = borrowings;
        this.return_date = return_date;
    }

    public String[] getBorrowings(){ return this.borrowings; }
    
    public Date getReturnDate(){ return this.return_date; }

}
