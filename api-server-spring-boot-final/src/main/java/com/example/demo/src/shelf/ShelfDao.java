package com.example.demo.src.shelf;

import com.example.demo.src.shelf.model.GetTotalShelfRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ShelfDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public GetTotalShelfRes GetTotalShelf(int sequence) {
        String query ="select book.id as book_id, title, author, file, image\n" +
                "from book inner join history_books on book.id = history_books.book_id ";

        switch (sequence){
            //최근 담은 순
            case 1: query = query + "where user_id = 3 order by history_books.created_at;";
                break;
            //제목
            case 2: query = query + "where user_id = 3 order by binary title;";
                break;
            // 작가
            case 3: query = query + "where user_id = 3 order by binary author;";
                break;
            case 4: query = query + "join book_info on book.id = book_info.book_id " +
                    "where user_id = 3 order by paper_date desc;";
                break;
            case 5: query = query + "left join (select book_id, max(created_at) as recent from history_log group by book_id) l on l.book_id = history_books.book_id " +
                    "where history_books.user_id = 3 order by l.recent desc";
                break;
            case 6: query = query + "where user_id = 3 order by binary publisher;";
                break;
        }
        GetTotalShelfRes getTotalShelfRes = new GetTotalShelfRes();
        getTotalShelfRes.setBookCnt(this.jdbcTemplate.queryForObject("select count(title) as book_cnt\n" +
                "from book\n" +
                "         inner join history_books on book.id = history_books.book_id\n" +
                "                     where history_books.user_id = 3;", int.class));
        getTotalShelfRes.setBooks(this.jdbcTemplate.queryForList(query));
        return getTotalShelfRes;
    }
}
