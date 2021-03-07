package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ShelfListBook {
    private int shelfId;
    private String slfTitle;
    private int bookCnt;
    private List<String> images;
    public ShelfListBook() {}
}
