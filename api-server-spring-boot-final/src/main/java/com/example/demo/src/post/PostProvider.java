package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.COMMENTS_EMPTY;
import static com.example.demo.config.BaseResponseStatus.POST_NOT_EXIST;

@Service
public class PostProvider {

    private final PostDao postDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService;
    }

    public GetPstRes getPostList(int intUserId) throws BaseException {
        if (intUserId == 0){
            int userJwtId = jwtService.getUserIdx();
            GetPstRes getPstRes = postDao.getMyPostList(userJwtId);
            return getPstRes;
        }
        GetPstRes getPstRes = postDao.getMyPostList(intUserId);
        return getPstRes;
    }

    public GetOnePstRes GetOnePost(int postId) throws BaseException {
        if (postDao.checkPostId(postId) == 0){
            throw new BaseException(POST_NOT_EXIST);
        }
        GetOnePstRes getOnePstRes = postDao.GetOnePost(postId);
        return getOnePstRes;
    }

    // get post-comments
    public GetPostComments getComments(int postId) throws BaseException {
        if (postDao.checkPostId(postId) == 0){
            throw new BaseException(POST_NOT_EXIST);
        }
        if (postDao.checkComments(postId) == 0){
            throw new BaseException(COMMENTS_EMPTY);
        }
        GetPostComments getPostComments = postDao.getComments(postId);
        return getPostComments;
    }

    // 유저의 포스트가 맞는지 확인
    public int checkPostUser( int userIdxJwt, int postId){
        return postDao.checkPostUser(userIdxJwt, postId);
    }

    // 유저의 책이 맞는지 체크
    public int checkUserBook(int bookId, int userIdJwt) {return postDao.checkUserBook(bookId, userIdJwt);}

    // 포스트 존재여부 체크
    public int checkPostId(int postId){return postDao.checkPostId(postId);}

    // 좋아요 한 글인지 체크
    public int checkLikes(int postId, int userId) { return postDao.checkLikes(postId, userId);}

    //내가 쓴 댓글인지 체크
    public int checkUserComment(int commentId, int userId) {return postDao.checkUserComment(commentId, userId);}

    // 포스트에 해당하는 댓글인지 체크
    public int checkPostComment(int postId, int commentId) {return postDao.checkPostComment(postId, commentId);}
}
