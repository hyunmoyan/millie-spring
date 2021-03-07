package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    private String userName;
    private int followingCnt;
    private int followedCnt;
    private int dayCnt;
    private int point;
}
