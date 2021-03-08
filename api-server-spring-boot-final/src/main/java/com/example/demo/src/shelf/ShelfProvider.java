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
            throw new BaseException(BaseResponseStatus.GET_SHELF_INVALID_ID);
        }

        GetShelfBooksRes getShelfBooksRes = shelfDao.getShelfBooks(shelfId, userIdxByJwt);
        return getShelfBooksRes;
    }

    //home에 있는 책인지 체크
    public int checkBookHome(PostShfBookReq postShfBookReq, int userIdJwt){
        return shelfDao.checkBookHome(postShfBookReq, userIdJwt);
    }

    // 책이 이미 존재하는지 체크 (패치 용)
    public int checkShfBook(PatchShelfReq postShfBookReq) {
        return shelfDao.checkPtjBooh(postShfBookReq);
    }

    //책이 이미 존재하는지 체크 (포스트 용)
    public int checkShfBook(PostShfBookReq postShfBookReq) {
        return shelfDao.checkShfBook(postShfBookReq);
    }

    //유저책장인지 체크
    public int checkUserShf(PostShfBookReq postShfBookReq, int userIdJwt) {
        return shelfDao.checkUserShf(postShfBookReq, userIdJwt);
    }

    public int checkUserShf(PatchShelfReq patchShelfReq, int userIdJwt ) {
        return shelfDao.checkUserShf(patchShelfReq, userIdJwt);
    }
}


