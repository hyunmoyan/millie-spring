package com.example.demo.src.book.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookReq {
    private String title;
    private String author;
    private String file;
    private String image;
    private int category;
}
