package com.example.demo.src.book.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookRes {
    private int CateId;
    private String CateName;
    private int BookID;
    private String Title;
    private String Author;
    private String Image;
}
