package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        return this.jdbcTemplate.query("select * from user",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("nickname"),
                        rs.getString("user"),
                        rs.getString("status"),
                        rs.getString("password"))
                );
    }

    public GetUserRes getUser(int userIdx){
        return this.jdbcTemplate.queryForObject("select * from user where id = ?",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("nickname"),
                        rs.getString("user"),
                        rs.getString("status"),
                        rs.getString("password")),
                userIdx);
    }


    public int createUser(PostUserReq postUserReq){
        this.jdbcTemplate.update("insert into user (nickname, user, password, phone_num) VALUES (?,?,?,?)",
                new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getPhoneNum()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkEmail(String email){
        return this.jdbcTemplate.queryForObject("select exists(select email from UserInfo where email = ?)",
                int.class,
                email);

    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select id, password, nickname, user from user where user = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("id"),
                        rs.getString("nickname"),
                        rs.getString("user"),
                        rs.getString("password")
                ),
                getPwdParams
        );

    }

}
