package com.example.demo.src.collection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GetShelfBooksRes {
    private int bookId;
    private String title;
    private String author;
    private String file;
    private String image;

    public GetShelfBooksRes() {
        
    }
}
