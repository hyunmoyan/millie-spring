package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPstRes {
    private int postCnt;
    private List<PostBrief> postBrief;
    public GetPstRes() {}
}
