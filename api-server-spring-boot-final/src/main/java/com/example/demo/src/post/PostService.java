package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.PostPstReq;
import com.example.demo.src.post.model.PostPstRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.POST_BOOKS_INVALID;
import static com.example.demo.config.BaseResponseStatus.POST_USER_DIFF;

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

    // update post!
    public String updatePost(PostPstReq postPstReq, int postId) throws BaseException {
        int userIdxJwt = jwtService.getUserIdx();
        // 유저의 포스트가 맞는지 확인
        if(postProvider.checkPostUser(userIdxJwt, postId) == 0){
            throw new BaseException(POST_USER_DIFF);
        }
        // 유저가 가진 포스트가 맞는지 체
        if (postDao.checkUserBook(postPstReq.getBookId(), userIdxJwt) == 0){
            throw new BaseException(POST_BOOKS_INVALID);
        }
        String msg = postDao.updatePost(postPstReq, postId);
        return msg;
    }
}
