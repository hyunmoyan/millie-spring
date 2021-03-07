package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShelfListBook {
    private String slfTitle;
    private int bookCnt;
    private String[] images;
    public ShelfListBook() {}
}
