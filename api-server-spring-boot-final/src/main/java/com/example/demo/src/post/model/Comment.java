package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private int parentId;
    private int commentId;
    private int userId;
    private String nickName;
    private String comment;
    private String date;
}
