package com.example.demo.src.shelf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetShelfRes {
    private int shelfCnt;
    private List<GetShelfBooksRes> getShelfBooksList;

    public GetShelfRes() {
        this.shelfCnt = shelfCnt;
        this.getShelfBooksList = getShelfBooksList;
    }
}
