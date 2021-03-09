package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOnePstRes {
    private int postId;
    private String title;
    private String userName;
    private String content;
    private String image;
    private int likesCnt;
    private int commentCnt;
}
