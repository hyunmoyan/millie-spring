package com.example.demo.src.shelf;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.shelf.model.PostShelfReq;
import com.example.demo.src.shelf.model.PostShelfRes;
import com.example.demo.src.shelf.model.PostShfBookReq;
import com.example.demo.src.shelf.model.PostShfBookRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

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

    public PostShelfRes createShelf(PostShelfReq postShelfReq) throws BaseException {
        int shelfId = shelfDao.createShelf(postShelfReq);
        return new PostShelfRes(shelfId);
    }

    public PostShfBookRes createShfBook(PostShfBookReq postShfBookReq) throws BaseException {
        // status가 N인 행이 있는 경우
        if(shelfProvider.checkShfBook(postShfBookReq) == 1){
            String message = shelfDao.createShfBook(postShfBookReq, 1);
        }
        if(shelfProvider.checkShfBook(postShfBookReq) == 2){
            throw new BaseException(BaseResponseStatus.POST_CHELFS_EXISTS);
        }
        // 테이블에 행이 존재하지 않는 경우
        String message = shelfDao.createShfBook(postShfBookReq, 0);
        return new PostShfBookRes(message);
    }

    public PostShfBookRes deleteShfBook(PostShfBookReq postShfBookReq) throws BaseException {
        if(shelfProvider.checkShfBook(postShfBookReq) == 1){
            throw new BaseException(BaseResponseStatus.PATCH_SHELFS_ALREADY_DELETED);
        }
        String message = shelfDao.deleteShfBook(postShfBookReq);
        return new PostShfBookRes(message);
    }
}
