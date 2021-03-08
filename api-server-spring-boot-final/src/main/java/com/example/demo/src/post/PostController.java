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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetPstRes> GetPostList() {
        try{
            GetPstRes getPstRes = postProvider.getPostList();
            return new BaseResponse<>(getPstRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
// 상세 포스트 보기
    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<GetOnePstRes> GetOnePost(@PathVariable("postId") int postId){
        try{
            GetOnePstRes getOnePstRes = postProvider.GetOnePost(postId);
            return new BaseResponse<>(getOnePstRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //[POST] 포스트 post API
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPstRes> PostPst(@RequestBody PostPstReq postPstReq) {
        if (postPstReq.getTitle().length() == 0| postPstReq.getTitle() == ""){
            return new BaseResponse<>(POST_EMPTY_TITLE);

        }
        if (postPstReq.getContent().length() == 0 | postPstReq.getContent() == ""){
            return new BaseResponse<>(POST_EMPTY_CONTENT);
        }
        try {
            PostPstRes postPstRes = postService.PostPst(postPstReq);
            return new BaseResponse<>(postPstRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
