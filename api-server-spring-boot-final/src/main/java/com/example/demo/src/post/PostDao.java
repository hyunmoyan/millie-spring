package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetPstRes getPostList(int bookJwtId) {
        List<Integer> postIds = this.jdbcTemplate.queryForList("select count(*) as post_cnt\n" +
                "from post" +
                " where user_id = ?", int.class, bookJwtId);
        GetPstRes getPstRes = new GetPstRes();
        getPstRes.setPostCnt(postIds.size());
        List<PostBrief> postBrief = this.jdbcTemplate.query("select post.id as post_id, title, content, image, likes_cnt, date_format(created_at, '%Y.%m.%d') as date\n" +
                        "from post\n" +
                        "         left join\n" +
                        "     (select post_id, count(post_id) as likes_cnt from post_likes where value = 'Y' group by post_id) likes\n" +
                        "     on likes.post_id = post.id where user_id = ? and status = 'Y'" +
                        "order by created_at desc",
                (rs, rowNum) -> new PostBrief(
                        rs.getInt("post_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("image"),
                        rs.getString("date"),
                        rs.getInt("likes_cnt")
                ),bookJwtId);
        getPstRes.setPostBrief(postBrief);
        return getPstRes;
    }

    public GetOnePstRes GetOnePost(int postId) {
        String query = "select post.id as post_id, title, nickname, content, post.image as image, likes_cnt, comment_cnt\n" +
                "from post join user on post.user_id = user.id\n" +
                "    left join (select post_id, count(post_id) as comment_cnt from post_comment where status = 'Y' group by post_id) com on com.post_id = post.id\n" +
                "         left join\n" +
                "     (select post_id, count(post_id) as likes_cnt from post_likes where value = 'Y' group by post_id) likes\n" +
                "     on likes.post_id = post.id where post.id = ?";
        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new GetOnePstRes(
                        rs.getInt("post_id"),
                        rs.getString("title"),
                        rs.getString("nickname"),
                        rs.getString("content"),
                        rs.getString("image"),
                        rs.getInt("likes_cnt"),
                        rs.getInt("comment_cnt")), postId);
    }

    public int postPst(PostPstReq postPstReq, int bookJwtId){
        this.jdbcTemplate.update("insert into post (title, content, image, book_id, user_id) VALUES (?,?,?,?,?)",
                new Object[]{postPstReq.getTitle(), postPstReq.getContent(), postPstReq.getImage(), postPstReq.getBookId()
                , bookJwtId});
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);

    }

    //put post! (update)
    public String updatePost(PostPstReq postPstReq, int bookJwtId){
        this.jdbcTemplate.update("insert into post (title, content, image, book_id, user_id) VALUES (?,?,?,?,?)",
                new Object[]{postPstReq.getTitle(), postPstReq.getContent(), postPstReq.getImage(), postPstReq.getBookId()
                        , bookJwtId});
        return "글이 수정되었습니다";
    }

    // patch post (delete)
    public String deletePost(int postId){
        String query = "update post set status='N' where post.id = ?";
        this.jdbcTemplate.update(query, String.class, postId);
        return "삭제가 완료되었습니다.";
    }
// check
    //post 존재 유무 확인
    public int checkPostId(int postId){
        String query = "select exists(select id from post where id = ? and status = 'Y')";
        return this.jdbcTemplate.queryForObject(query, int.class, postId);
    }

    // 유저의 포스트가 맞는지 확인
    public int checkPostUser(int userIdxJwt, int postId ){
        String query = "select exists(select id from post where post.id = ? and user_id = ? and status= 'Y')";
        return this.jdbcTemplate.queryForObject(query, int.class, new Object[]{postId, userIdxJwt});
    }

    // 책이 읽은 목록에 있는지 확인
    public int checkUserBook(int bookId, int userIdJwt){
        String query = "select exists(select book_id from history_books where book_id = ? and user_id = ? and status= 'Y')";
        return this.jdbcTemplate.queryForObject(query , int.class, new Object[]{bookId, userIdJwt});
    }
}
