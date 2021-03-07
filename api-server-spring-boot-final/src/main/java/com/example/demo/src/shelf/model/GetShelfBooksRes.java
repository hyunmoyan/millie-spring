package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GetShelfBooksRes {
    private String shfName;
    private int bookCnt;
    private List<Map<String, Object>> books;

    public GetShelfBooksRes() {

    }
}
