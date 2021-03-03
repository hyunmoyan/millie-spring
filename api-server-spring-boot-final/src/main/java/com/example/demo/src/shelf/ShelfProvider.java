package com.example.demo.src.shelf;

import com.example.demo.src.shelf.model.GetTotalShelfRes;
import com.example.demo.src.shelf.model.PostShfBookReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

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

    public GetTotalShelfRes GetShelfBookList(int sequence) {
        GetTotalShelfRes getTotalShelfRes =  shelfDao.GetTotalShelf(sequence);
        return getTotalShelfRes;
    }

    public int checkShfBook(PostShfBookReq postShfBookReq) {
        return shelfDao.checkShfBook(postShfBookReq);
    }
}


