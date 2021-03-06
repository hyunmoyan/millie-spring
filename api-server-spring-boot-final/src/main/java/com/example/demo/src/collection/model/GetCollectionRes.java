package com.example.demo.src.collection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GetCollectionRes {
    private String title;
    private int bookCnt;
    private List<Map<String, Object>> books;

    public GetCollectionRes() {
    }
}

