package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int postPst(PostPstReq postPstReq, int bookJwtId){
        this.jdbcTemplate.update("insert into post (title, content, image, book_id, user_id) VALUES (?,?,?,?,?)",
                new Object[]{postPstReq.getTitle(), postPstReq.getContent(), postPstReq.getImage(), postPstReq.getBookId()
                , bookJwtId});
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);

    }
}
