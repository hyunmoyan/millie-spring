package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetShelfListRes {
    private int shelfCnt;
    private List<ShelfListBook> shelfList;
    public GetShelfListRes() {
    }
}
