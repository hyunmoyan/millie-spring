package com.example.demo.src.shelf;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.shelf.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class ShelfService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ShelfDao shelfDao;
    private final ShelfProvider shelfProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource); }

    @Autowired
    public ShelfService(ShelfDao shelfDao, ShelfProvider shelfProvider, JwtService jwtService) {
        this.shelfDao = shelfDao;
        this.shelfProvider = shelfProvider;
        this.jwtService = jwtService;
    }

    @Transactional
    public PostShelfRes createShelf(PostShelfReq postShelfReq) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        int shelfId = shelfDao.createShelf(postShelfReq);
        return new PostShelfRes(shelfId);
    }

    @Transactional
    public PostShfBookRes createShfBook(PostShfBookReq postShfBookReq) throws BaseException {
        // status가 N인 행이 있는 경우
        /*if(shelfProvider.checkShfBook(postShfBookReq) == 1){
            String message = shelfDao.createShfBook(postShfBookReq, 1);
        }*/

        //유저게 책장이 있는지 체크
        int userIdJwt = jwtService.getUserIdx();

        // home에 있는 책인지 체크
        if(shelfProvider.checkBookHome(postShfBookReq, userIdJwt) == 0){
            throw new BaseException(BaseResponseStatus.POST_BOOKS_INVALID);
        }

        // 유저의 책장인지 체
        if(shelfProvider.checkUserShf(postShfBookReq, userIdJwt)==0){
            throw new BaseException(BaseResponseStatus.POST_SHELFS_INVAILD_USER);
        }

        //책이 이미 존재!
        if(shelfProvider.checkShfBook(postShfBookReq) == 1){
            throw new BaseException(BaseResponseStatus.POST_CHELFS_EXISTS);
        }

        // 테이블에 행이 존재하지 않는 경우
        String message = shelfDao.createShfBook(postShfBookReq);
        return new PostShfBookRes(message);
    }

    @Transactional
    public PostShfBookRes deleteShfBook(PatchShelfReq patchShelfReq) throws BaseException {

        int userIdJwt = jwtService.getUserIdx();

        if(shelfProvider.checkUserShf(patchShelfReq, userIdJwt)==0){
            throw new BaseException(BaseResponseStatus.POST_SHELFS_INVAILD_USER);
        }

        if(shelfProvider.checkShfBook(patchShelfReq) == 1){
            throw new BaseException(BaseResponseStatus.PATCH_SHELFS_ALREADY_DELETED);
        }
        String message = shelfDao.deleteShfBook(patchShelfReq);
        return new PostShfBookRes(message);
    }
}
