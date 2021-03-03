package com.example.demo.src.shelf;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.shelf.model.GetShelfBooksRes;
import com.example.demo.src.shelf.model.GetTotalShelfRes;
import com.example.demo.src.shelf.model.PostShfBookReq;
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

    public GetTotalShelfRes GetShelfsBookList(int sequence) {
        GetTotalShelfRes getTotalShelfRes =  shelfDao.getTotalShelf(sequence);
        return getTotalShelfRes;
    }

    public GetShelfBooksRes getShelfBooks(int shelfId) throws BaseException{
        if(shelfDao.checkShfId(shelfId) == 0){
            throw new BaseException(BaseResponseStatus.POST_BOOKS_EXITS_TITLE);
        }
        GetShelfBooksRes getShelfBooksRes = shelfDao.getShelfBooks(shelfId);
        return getShelfBooksRes;
    }

    public int checkShfBook(PostShfBookReq postShfBookReq) {
        return shelfDao.checkShfBook(postShfBookReq);
    }
}


