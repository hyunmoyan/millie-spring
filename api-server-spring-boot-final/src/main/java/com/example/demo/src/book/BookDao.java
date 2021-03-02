package com.example.demo.src.book;


import com.example.demo.src.book.model.*;
import com.example.demo.src.user.model.GetUserRes;
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

    public GetBookInfoRes getBookInfo(int bookId){
        return this.jdbcTemplate.queryForObject(
                "select book.id as book_id,\n" +
                        "       title,\n" +
                        "       image,\n" +
                        "       author,\n" +
                        "       subtitle,\n" +
                        "       intro,\n" +
                        "       length,\n" +
                        "       capacity,\n" +
                        "       paper_date,\n" +
                        "       ebook_date,\n" +
                        "       contents,\n" +
                        "       category.name,\n" +
                        "       user_cnt,\n" +
                        "       post_cnt\n" +
                        "from book\n" +
                        "    join book_info on book.id = book_info.book_id\n" +
                        "    left join (select history_books.book_id, count(history_books.book_id) as user_cnt, post_cnt from history_books left join (select book_id, count(book_id) as post_cnt from post group by book_id) p on history_books.book_id = p.book_id group by history_books.book_id)\n" +
                        "    c on c.book_id = book.id\n" +
                        "    left join category on book.category = category.category_id\n" +
                        "    where book.id = ?;",
                (rs, rowNum) -> new GetBookInfoRes(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("image"),
                        rs.getInt("user_cnt"),
                        rs.getInt("post_cnt"),
                        rs.getString("subtitle"),
                        rs.getString("intro"),
                        rs.getInt("length"),
                        rs.getInt("capacity"),
                        rs.getString("paper_date"),
                        rs.getString("ebook_date"),
                        rs.getString("contents"),
                        rs.getString("category.name")
                        ),
                bookId);

    }

    public List<GetBookRes> getBooks(String category){
        if (category == null || category.equals("")){
            return this.jdbcTemplate.query("select category_id, category.name as category_name, book.id as book_id, title, author, image  from book\n" +
                            "join category on category.category_id = category;",
                    (rs, rowNum) -> new GetBookRes(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("image")
                    ));
        }
        return this.jdbcTemplate.query("select category_id, category.name as category_name, book.id as book_id, title, author, image  from book\n" +
                        "join category on category.category_id = category where category.category_id = ?;",
                (rs, rowNum) -> new GetBookRes(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("image")
                ),
                category);
    }

    public int createBook(PostBookReq postBookReq){
        this.jdbcTemplate.update("insert into book (title, author, file, image, category) values (?,?,?,?,?);",
                new Object[]{postBookReq.getTitle(), postBookReq.getAuthor(), postBookReq.getFile(), postBookReq.getImage(), postBookReq.getCategory()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkTitle(String title){
        return this.jdbcTemplate.queryForObject("select exists(select title from book where title = ?)",
                int.class,
                title);
    }
    public int checkId(int bookId){
        return this.jdbcTemplate.queryForObject("select exists(select id from book where id = ?)",
                int.class,
                bookId);
    }
}
