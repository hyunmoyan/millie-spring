package com.example.demo.src.post;

import com.example.demo.src.post.PostProvider;
import com.example.demo.src.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_JWT;

@RestController
@RequestMapping("/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;


    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }

    //[POST] 포스트 post API
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPstRes> PostPst(@RequestBody PostPstReq postPstReq) {
        try {
            PostPstRes postPstRes = postService.PostPst(postPstReq);
            return new BaseResponse<>(postPstRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
