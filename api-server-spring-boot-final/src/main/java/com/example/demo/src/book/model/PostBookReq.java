package com.example.demo.src.book.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostBookReq {
    private String title;
    private String author;
    private String file;
    private String image;
    private int category;
}
