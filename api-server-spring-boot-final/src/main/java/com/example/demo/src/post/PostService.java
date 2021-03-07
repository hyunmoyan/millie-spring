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

    public PostPstRes PostPst(PostPstReq postPstReq) throws BaseException {
        int bookJwtId = jwtService.getUserIdx();
        int postId = postDao.postPst(postPstReq, bookJwtId);
        return new PostPstRes(postId);
    }
}
