package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBrief {
    private int postId;
    private String title;
    private String content;
    private String image;
    private String date;
    private int likeCnt;

}
