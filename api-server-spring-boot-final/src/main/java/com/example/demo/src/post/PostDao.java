package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import lombok.val;
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

    public GetPstRes getMyPostList(int userId) {
        List<Integer> postIds = this.jdbcTemplate.queryForList("select count(*) as post_cnt\n" +
                "from post" +
                " where user_id = ?", int.class, userId);
        GetPstRes getPstRes = new GetPstRes();
        getPstRes.setPostCnt(postIds.size());
        List<PostBrief> postBrief = this.jdbcTemplate.query("select post.id as post_id, title, content, image, likes_cnt, date_format(created_at, '%Y.%m.%d') as date\n" +
                        "from post\n" +
                        "         left join\n" +
                        "     (select post_id, count(post_id) as likes_cnt from post_likes where status = 'Y' group by post_id) likes\n" +
                        "     on likes.post_id = post.id where user_id = ? and status = 'Y'" +
                        "order by created_at desc",
                (rs, rowNum) -> new PostBrief(
                        rs.getInt("post_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("image"),
                        rs.getString("date"),
                        rs.getInt("likes_cnt")
                ),userId);
        getPstRes.setPostBrief(postBrief);
        return getPstRes;
    }

    public GetOnePstRes GetOnePost(int postId) {
        String query = "select post.id as post_id, title, nickname, content, post.image as image, likes_cnt, comment_cnt\n" +
                "from post join user on post.user_id = user.id\n" +
                "    left join (select post_id, count(post_id) as comment_cnt from post_comment where status = 'Y' and parent=0 group by post_id) com on com.post_id = post.id\n" +
                "         left join\n" +
                "     (select post_id, count(post_id) as likes_cnt from post_likes where status = 'Y' group by post_id) likes\n" +
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

    // get comments
    public GetPostComments getComments(int postId){
        //comment likes
        int commentCnt = this.jdbcTemplate.queryForObject("select count(*) from post_comment where post_id = ? and status='Y' and parent=0", int.class, postId);
        //comment list
        String query = "select parent, post_comment.id as comment_id, user_id, nickname, comment, date_format(post_comment.created_at, '%m.%d %H:%i')  as date " +
                "from post_comment join user on post_comment.user_id = user.id where post_id = ? and post_comment.status = 'Y' ";
        List<Comment> parentComments = this.jdbcTemplate.query(query + "and parent=0",
                (rs, rowNum) -> new Comment(
                        0,
                        rs.getInt("comment_id"),
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("comment"),
                        rs.getString("date")
                )
                , postId);
        List<Comment> childComments = this.jdbcTemplate.query(query + "and parent!=0",
                (rs, rowNum) -> new Comment(
                        rs.getInt("parent"),
                        rs.getInt("comment_id"),
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("comment"),
                        rs.getString("date")
                )
                , postId);
        GetPostComments getPostComments = new GetPostComments(commentCnt, parentComments, childComments);
        return getPostComments;
    }

    public int postPst(PostPstReq postPstReq, int bookJwtId){
        this.jdbcTemplate.update("insert into post (title, content, image, book_id, user_id) VALUES (?,?,?,?,?)",
                new Object[]{postPstReq.getTitle(), postPstReq.getContent(), postPstReq.getImage(), postPstReq.getBookId()
                , bookJwtId});
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);

    }
    //create comment
    public String createComment(Comment comment, int postId){
        String query = "insert into post_comment (parent, user_id, post_id, comment) values (?,?,?,?)";
        this.jdbcTemplate.update(query,
                new Object[]{comment.getParentId(), comment.getUserId(), postId, comment.getComment()});
        if(comment.getParentId() == 0){
            return "댓글이 등록되었습니다";
        }
        return "대댓글이 등록되었습니다.";
    }
    //creat likes
    public int createLikes(int postId, int userId){
        String query = "insert into post_likes (post_id, user_id) values (?,?)";
        this.jdbcTemplate.update(query, new Object[]{postId, userId});
        return 1;
    }

    //put post! (update)
    public String updatePost(PostPstReq postPstReq, int postId){
        this.jdbcTemplate.update("update post set title=?, content=?, image=?, book_id=? where post.id= ?",
                new Object[]{postPstReq.getTitle(), postPstReq.getContent(), postPstReq.getImage(), postPstReq.getBookId()
                        , postId});
        return "글이 수정되었습니다";
    }

    // update likes to unlikes
    public int updateLikes(int postId, int userId){
        String query = "update post_likes set status='N' where post_id = ? and user_id = ?";
        this.jdbcTemplate.update(query, new Object[]{postId, userId});
        return 0;
    }

    // patch post (delete)
    public String deletePost(int postId){
        String query = "update post set status='N' where id = ?";
        if(this.jdbcTemplate.update(query, postId) == 1){
            return "삭제가 완료되었습니다.";
        }
        return "삭제가 되지 않음";
    }

    // delete comments
    public String deleteComment(int commentId){
        String query = "update post_comment set status='N' where id = ?";
        if(this.jdbcTemplate.update(query, commentId) == 1){
            return "댓글이 삭제되었습니다.";
        }
        return "댓글 삭제 실패";
    }

    // update comment
    public String updateComment(PutCommentReq putCommentReq){
        String query = "update post_comment set comment=? where id=?";
        this.jdbcTemplate.update(query , putCommentReq.getComment(), putCommentReq.getCommentId());
        return "댓글이 수정되었습니다.";
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

    // 좋아요 된 글인지 확인
    public int checkLikes(int postId, int userId){
        String query = "select exists(select id from post_likes where post_id = ? and user_id = ? and status= 'Y')";
        return this.jdbcTemplate.queryForObject(query , int.class, new Object[]{postId, userId});
    }

    // 댓글이 있는지 확인
    public int checkComments(int postId){
        String query = "select exists(select id from post_comment where post_id=? and parent=0 and status='Y')";
        return this.jdbcTemplate.queryForObject(query, int.class, postId);
    }

    // 자신의 댓글인지 확인
    public int checkUserComment(int commentId, int userId){
        String query = "select exists(select id from post_comment where id=? and user_id=? and status='Y')";
        return this.jdbcTemplate.queryForObject(query, int.class, commentId, userId);
    }

    public int checkPostComment(int postId, int commentId){
        String query = "select exists(select id from post_comment where id=? and post_id = ?)";
        return this.jdbcTemplate.queryForObject(query, int.class, commentId, postId);
    }
}
