package com.example.demo.src.book.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookInfoRes {
    private int bookId;
    private String title;
    private String author;
    private String image;
    private int userCnt;
    private int postCnt;
    private String subTitle;
    private String intro;
    private int length;
    private int capacity;
    private String paperDate;
    private String ebookDate;
    private String contents;
    private String cateName;
}
