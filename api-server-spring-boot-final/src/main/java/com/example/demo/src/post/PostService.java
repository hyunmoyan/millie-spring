package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.Comment;
import com.example.demo.src.post.model.PostPstReq;
import com.example.demo.src.post.model.PostPstRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
    }
// create post!
    public PostPstRes PostPst(PostPstReq postPstReq) throws BaseException {
        int bookJwtId = jwtService.getUserIdx();
        int postId = postDao.postPst(postPstReq, bookJwtId);
        return new PostPstRes(postId);
    }

// create comment
    public String createComment(Comment comment, int postId) throws BaseException {
        int bookJwtId = jwtService.getUserIdx();
        if(postProvider.checkPostId(postId)==0){
            throw new BaseException(POST_NOT_EXIST);
        }
        comment.setUserId(bookJwtId);
        String msg = postDao.createComment(comment, postId);
        return msg;
    }

// create and delete likes
    public int postLikesUnlikes(int postId) throws BaseException{
        int userIdxJwt = jwtService.getUserIdx();
        // 포스트 존재 확인
        if(postProvider.checkPostId(postId)==0){
            throw new BaseException(POST_NOT_EXIST);
        }
        // 이미 좋아한 글인지 확인, 좋아요 -> update
        // unlikes == 0
        if(postProvider.checkLikes(postId, userIdxJwt)==1){
            int unlikes =  postDao.updateLikes(postId, userIdxJwt);
            return unlikes;
        }
        // not like -> create likes
        // likes == 1
        int likes = postDao.createLikes(postId, userIdxJwt);
        return likes;
    }
    // update post!
    public String updatePost(PostPstReq postPstReq, int postId) throws BaseException {
        int userIdxJwt = jwtService.getUserIdx();
        if(postProvider.checkPostId(postId)==0){
            throw new BaseException(POST_NOT_EXIST);
        }
        // 유저의 포스트가 맞는지 확인
        if(postProvider.checkPostUser(userIdxJwt, postId) == 0){
            throw new BaseException(POST_USER_DIFF);
        }
        // 유저가 가진 책이 맞는지 체
        if (postProvider.checkUserBook(postPstReq.getBookId(), userIdxJwt) == 0){
            throw new BaseException(POST_BOOKS_INVALID);
        }
        String msg = postDao.updatePost(postPstReq, postId);
        return msg;
    }
    // delete post
    public String deletePost(int postId) throws BaseException {
        int userIdxJwt = jwtService.getUserIdx();
        // 포스터 존재여부
        if(postProvider.checkPostId(postId)==0){
            throw new BaseException(POST_NOT_EXIST);
        }
        // 유저의 포스트가 맞는지 확인
        if(postProvider.checkPostUser(userIdxJwt, postId) == 0){
            throw new BaseException(POST_USER_DIFF);
        }
        String msg = postDao.deletePost(postId);
        return msg;
    }

    //delete comments
    public String deleteComment(int postId, int commentId) throws BaseException{
        if(postProvider.checkPostId(postId)==0){
            throw new BaseException(POST_NOT_EXIST);
        }
        // 포스트에 댓글이 존재하는지 여부
        if(postProvider.checkPostComment(postId, commentId) == 0){
            throw new BaseException(COMMENT_USER_DIFF);
        }
        int userIdxJwt = jwtService.getUserIdx();
        //내가 쓴 댓글인지 확
        if(postProvider.checkUserComment(commentId, userIdxJwt) == 0){
            throw new BaseException(COMMENT_USER_DIFF);
        }
        String msg = postDao.deleteComment(commentId);
        return msg;
    }
}
