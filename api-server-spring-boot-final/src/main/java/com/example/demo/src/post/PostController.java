package com.example.demo.src.post;

import com.example.demo.src.post.PostProvider;
import com.example.demo.src.post.PostService;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
// 내 포스트 리스트 받
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetPstRes> GetPostList(@RequestParam(required = false, defaultValue = "0") String user) {
        int intUserId = Integer.parseInt(user);
        try{
            GetPstRes getPstRes = postProvider.getPostList(intUserId);
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

    // [get] /post/{post_id}/comments
    @ResponseBody
    @GetMapping("/{postId}/comments")
    public BaseResponse<GetPostComments> getComments(@PathVariable("postId") int postId){
        try{
            GetPostComments getPostComments = postProvider.getComments(postId);
            return new BaseResponse<>(getPostComments);
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
    // [post] /post/{post_id}/likes
    @ResponseBody
    @PostMapping("/{postId}/likes")
    public BaseResponse<Integer> postLikesUnlikes(@PathVariable("postId") int postId){
        try {
            int likes = postService.postLikesUnlikes(postId);
            return new BaseResponse<>(likes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("{postId}/comments")
    public BaseResponse<String> createComment(@PathVariable("postId") int postId, @RequestBody Comment comment){
        if(comment.getComment()==null|comment.getComment()==""){
            return new BaseResponse<>(COMMENT_EMPTY);
        }
        try {
            String msg = postService.createComment(comment, postId);
            return new BaseResponse<>(msg);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    // [put] update post
    @ResponseBody
    @PutMapping("/{postId}")
    public BaseResponse<String> updatePost(@PathVariable("postId") int postId, @RequestBody PostPstReq postPstReq){
        if (postPstReq.getTitle() == null| postPstReq.getTitle() == ""){
            return new BaseResponse<>(POST_EMPTY_TITLE);
        }
        if (postPstReq.getContent() == null | postPstReq.getContent() == ""){
            return new BaseResponse<>(POST_EMPTY_CONTENT);
        }
        try {
            String msg = postService.updatePost(postPstReq, postId);
            return new BaseResponse<>(msg);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PutMapping("/{postId}/comments")
    public BaseResponse<String> updateComment(@PathVariable("postId") int postId, @RequestBody PutCommentReq putCommentReq){
        if(putCommentReq.getComment()==null| putCommentReq.getComment()==""){
            return new BaseResponse<>(COMMENT_EMPTY);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //유저와 다를 경
            if(putCommentReq.getUserId() != userIdxByJwt){
                return new BaseResponse<>(COMMENT_USER_DIFF);
            }
            String msg = postService.updateComment(putCommentReq, postId);
            return new BaseResponse<>(msg);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // [patch] delete post
    @ResponseBody
    @PatchMapping("/{postId}")
    public BaseResponse<String> deletePost(@PathVariable("postId") int postId){
        try {
            String msg = postService.deletePost(postId);
            return new BaseResponse<>(msg);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // [patch] delete comment
    @ResponseBody
    @PatchMapping("/{postId}/comments/{commentId}")
    public BaseResponse<String> deleteComment(@PathVariable("postId") int postId
            , @PathVariable("commentId") int commentId){
        try {
            String msg = postService.deleteComment(postId, commentId);
            return new BaseResponse<>(msg);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
