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

    public List<GetUserInfoRes> getUsers(int userIdx){
        String query = "select nickname, follower_cnt, following_cnt, datediff(now(), created_at) as day_cnt, point\n" +
                "from user\n" +
                "         inner join (select user_id as id, ifnull(count(user_id), 0) as point\n" +
                "                     from history_log\n" +
                "                     group by user_id) point_tb on user.id = point_tb.id\n" +
                "         inner join\n" +
                "     (select follower.id as id, follower_cnt, following_cnt\n" +
                "      from (select user.id as id, ifnull(count(follower_id), 0) as follower_cnt\n" +
                "            from follow\n" +
                "                     right join user on user.id = follower_id\n" +
                "            group by id) follower\n" +
                "               inner join (select user.id as id, ifnull(count(following_id), 0) as following_cnt\n" +
                "                           from follow\n" +
                "                                    right join user on user.id = following_id\n" +
                "                           group by id) following on following.id = follower.id) follow_tb on follow_tb.id = user.id\n" +
                "where user.id = ?;";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("nickname"),
                        rs.getInt("follower_cnt"),
                        rs.getInt("following_cnt"),
                        rs.getInt("day_cnt"),
                        rs.getInt("point")), userIdx
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
