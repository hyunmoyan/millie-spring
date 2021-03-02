package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GetShelfRes {
    private String title;
    private GetShelfRes books;

    public GetShelfBooksRes() {
        this.title = title;
        this.books = books;
    }
}
