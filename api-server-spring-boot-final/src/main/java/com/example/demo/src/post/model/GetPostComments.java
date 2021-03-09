package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostComments {
    int commentCnt;
    private List<Comment> parentComments;
    private List<Comment> childComments;
}
