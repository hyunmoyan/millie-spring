package com.example.demo.src.shelf;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.shelf.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.GET_SHELF_INVALID_ID;

@Service
public class ShelfProvider {

    private final ShelfDao shelfDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource);}

    @Autowired
    public ShelfProvider(ShelfDao shelfDao, JwtService jwtService){
        this.shelfDao = shelfDao;
        this.jwtService = jwtService;
    }

    public GetShelfListRes getShelfList() throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        GetShelfListRes getShelfListRes = shelfDao.getShelfList(userIdxByJwt);
        return getShelfListRes;
    }

    public GetTotalShelfRes GetShelfsBookList(int sequence) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        GetTotalShelfRes getTotalShelfRes =  shelfDao.getTotalShelf(sequence, userIdxByJwt);
        return getTotalShelfRes;
    }

    public GetShelfBooksRes getShelfBooks(int shelfId) throws BaseException{
        int userIdxByJwt = jwtService.getUserIdx();
        if(shelfDao.checkShfId(shelfId) == 0){
            throw new BaseException(BaseResponseStatus.POST_BOOKS_EXITS_TITLE);
        }

        GetShelfBooksRes getShelfBooksRes = shelfDao.getShelfBooks(shelfId, userIdxByJwt);
        return getShelfBooksRes;
    }

    public int checkShfBook(PatchShelfReq postShfBookReq) {
        return shelfDao.checkPtjBooh(postShfBookReq);
    }

    public int checkShfBook(PostShfBookReq postShfBookReq) {
        return shelfDao.checkShfBook(postShfBookReq);
    }

    public int checkUserShf(PostShfBookReq postShfBookReq, int userIdJwt) {
        return shelfDao.checkUserShf(postShfBookReq, userIdJwt);
    }

    public int checkUserShf(PatchShelfReq patchShelfReq, int userIdJwt ) {
        return shelfDao.checkUserShf(patchShelfReq, userIdJwt);
    }
}


