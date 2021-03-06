package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        int userIdx = userDao.createUser(postUserReq);

        //jwt발급
        String jwt = jwtService.createJwt(userIdx);
        return new PostUserRes(jwt,userIdx);
    }
    @Transactional
    public String createFollow(int userIdtoflw) throws BaseException{
        int userId = jwtService.getUserIdx();
        //유저가 존재하지 않는 경
       if(userProvider.checkUser(userIdtoflw)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
       //이미 팔로우하고 있을때
       if(userProvider.checkFollow(userId, userIdtoflw)==1){
           String msg = userDao.upadateUnFollow(userId, userIdtoflw);
           return msg;
       }
       String msg = userDao.createFollow(userId, userIdtoflw);
       return msg;
    }
}
