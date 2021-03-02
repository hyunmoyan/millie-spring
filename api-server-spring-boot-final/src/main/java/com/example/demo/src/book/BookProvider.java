package com.example.demo.src.book;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.book.model.GetBookInfoRes;
import com.example.demo.src.book.model.GetBookRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BookProvider {

    private final BookDao bookDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public BookProvider(BookDao bookDao, JwtService jwtService) {
        this.bookDao = bookDao;
        this.jwtService = jwtService;
    }

    public GetBookInfoRes getBookInfo(int bookId) throws BaseException {
        if(bookDao.checkId(bookId)==0){
            throw new BaseException(BaseResponseStatus.POST_BOOKS_INVALID_NUMBER);
        }
        GetBookInfoRes getBookInfoRes = bookDao.getBookInfo(bookId);
        return getBookInfoRes;
    }

    public List<GetBookRes> getBooks(String category){
        List<GetBookRes> getBooksRes = bookDao.getBooks(category);
        return getBooksRes;
    }

    public int checkTitle(String title) { return bookDao.checkTitle(title); }
}
