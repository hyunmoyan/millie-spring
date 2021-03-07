package com.example.demo.src.shelf.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchShelfReq {
    private int bookId[];
    private int shelfId;
}
