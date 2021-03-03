package com.example.demo.src.shelf;

import com.example.demo.src.shelf.model.GetShelfBooksRes;
import com.example.demo.src.shelf.model.GetTotalShelfRes;
import com.example.demo.src.shelf.model.PostShelfReq;
import com.example.demo.src.shelf.model.PostShfBookReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ShelfDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public GetTotalShelfRes getTotalShelf(int sequence) {
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

    public GetShelfBooksRes getShelfBooks(int shelfId) {
        String query = "select count(shelf_books.book_id) as book_cnt, shelf.name as name, shelf_books.book_id as book_id, title, author, image, concat(percentage, \"%\") as percent,\n" +
                "       if(datediff(now(), shelf_books.created_at) > 30, '만료', datediff(now(), shelf_books.created_at)) as exist_day\n" +
                "from shelf_books\n" +
                "left join history_books on shelf_books.id = history_books.book_id join book on book.id = shelf_books.book_id  join shelf on shelf_books.shelf_id = shelf.id\n" +
                "    where shelf_books.shelf_id = ? and history_books.user_id = 1;";
        GetShelfBooksRes getShelfBooksRes = new GetShelfBooksRes();
        getShelfBooksRes.setShfName(this.jdbcTemplate.queryForObject("select name from shelf where shelf.id = ?;", String.class,shelfId));
        getShelfBooksRes.setBooks(this.jdbcTemplate.queryForList(query, shelfId));
        return getShelfBooksRes;
    }

    public int createShelf(PostShelfReq postShelfReq){
        this.jdbcTemplate.update("insert into shelf (name, user_id) values (?,?);",
                new Object[]{postShelfReq.getName(), postShelfReq.getUserId()});
        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

    public String createShfBook(PostShfBookReq postShfBookReq, int status){
        switch (status){
            // 이미 테이블에 존재할 때
            case 0 : this.jdbcTemplate.update("insert into shelf_books (shelf_id, book_id) values (?,?);",
                    new Object[]{postShfBookReq.getShelfId(), postShfBookReq.getBookId()});
                break;
                // 테이블에 존재하지 않을
            case 1 : this.jdbcTemplate.update("update shelf_books set status='Y' where book_id = ? and shelf_id = ?;",
                    new Object[]{postShfBookReq.getBookId(), postShfBookReq.getShelfId()});
                break;
        }
        return "추가 되었습니다.";
    }
    public String deleteShfBook(PostShfBookReq postShfBookReq){
        this.jdbcTemplate.update("update shelf_books set status='N' where book_id = ? and shelf_id = ?;"
            , postShfBookReq.getBookId(), postShfBookReq.getShelfId());
        return "삭제 되었습니다.";
    }

    public int checkShfBook(PostShfBookReq postShfBookReq){
        if (this.jdbcTemplate.queryForObject("select exists(select book_id from shelf_books where book_id = ? " +
                "and shelf_id = ? and status= 'Y');", int.class, postShfBookReq.getBookId(), postShfBookReq.getShelfId()) == 1){
            return 2;
        }
        return this.jdbcTemplate.queryForObject("select exists(select book_id from shelf_books where book_id = ? " +
                "and shelf_id = ? and status= 'N');", int.class, postShfBookReq.getBookId(), postShfBookReq.getShelfId());
    }

    public int checkShfId(int shelfId){
        return this.jdbcTemplate.queryForObject("select exists(select shelf.id from shelf where shelf.id = ?);",
                int.class, shelfId);
    }
}
