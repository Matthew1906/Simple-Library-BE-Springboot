package com.learn.simple_library_be.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="member")
public class Member {
    
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique=true)
    private String code;

    @Column(nullable = false)
    private String name;
    
    private String email, phone_no;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("member")
    private List<Borrowing> borrowings;

    protected Member() { }

    public Member(String code, String name, String email, String phone_no){
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
    }

    @Override
    public String toString(){
        return String.format("Member  [id:%s, code:%s, name:%s, email:%s, phone:%s]", this.id, this.code, this.name, this.email, this.phone_no);
    }

    public String getId(){ return this.id; }

    public String getCode(){ return this.code; }

    public String getName(){ return this.name; }

    public String getEmail(){ return this.email; }

    public String getPhone(){ return this.phone_no; }

    public List<Borrowing> getBorrowings(){ return this.borrowings; }

    public void setCode(String code){ this.code = code; }

    public void setName(String name){ this.name = name; }

    public void setEmail(String email){ this.email = email; }

    public void setPhone(String phone){ this.phone_no = phone; }

}