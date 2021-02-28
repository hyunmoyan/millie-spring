package com.example.demo.src.book;


import com.example.demo.src.book.model.*;
import com.example.demo.src.book.model.GetBookRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BookDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetBookRes getBook(int collectionId){
        GetBookRes getBookRes = new GetBookRes();
        getBookRes.setList(this.jdbcTemplate.queryForList("select collection.id as collection_id, book.id as book_id, book.title as title, author, image from book join collection_books on book.id = collection_books.book_id\n" +
                "inner join collection on collection_books.collection_id = collection.id where collection.id = ?;", collectionId));
        getBookRes.setTitle(this.jdbcTemplate.queryForObject("select title from collection where collection.id = ?;", String.class, collectionId));
        return getBookRes;
    }
}
