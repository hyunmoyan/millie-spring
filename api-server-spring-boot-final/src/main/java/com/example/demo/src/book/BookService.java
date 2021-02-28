package com.example.demo.src.book;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.book.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BookService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookDao bookDao;
    private final BookProvider bookProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public BookService(BookDao bookDao, BookProvider bookProvider, JwtService jwtService) {
        this.bookDao = bookDao;
        this.bookProvider = bookProvider;
        this.jwtService = jwtService;

    }
}
