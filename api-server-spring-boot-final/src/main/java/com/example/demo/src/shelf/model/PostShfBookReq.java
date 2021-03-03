package com.example.demo.src.shelf.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShfBookReq {
    private int shelfId;
    private int bookId;
    PostShfBookReq() {
        this.shelfId = 0;
        this.bookId = 0;
    }
}
