package com.example.demo.src.shelf.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShfBookReq {
    private int shelfId = 0;
    private int bookId[];
}
