package com.example.demo.src.shelf;

import com.example.demo.src.shelf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ShelfDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public GetShelfListRes getShelfList(int userIdxByJwt) {
        List<ShelfListBook> shelfList = new ArrayList<>();
        List<Integer> shelf = this.jdbcTemplate.queryForList("select id from shelf where shelf.user_id = ?"
                    , int.class, userIdxByJwt);

        for(int i=0; i < shelf.size(); i++){
            ShelfListBook shelfListBook = new ShelfListBook();
            shelfListBook.setSlfTitle(this.jdbcTemplate.queryForObject("select name from shelf where shelf.id = ?", String.class ,shelf.get(i)));
            shelfListBook.setBookCnt(this.jdbcTemplate.queryForObject("select count(*) from shelf_books where shelf_id= ?", int.class, shelf.get(i)));
            shelfListBook.setImages(this.jdbcTemplate.queryForList("select image from shelf_books join book on shelf_books.book_id = book.id where shelf_books.shelf_id = ? limit 5"
                                    , String.class ,shelf.get(i)));
            shelfList.add(shelfListBook);
        }


        GetShelfListRes getShelfListRes = new GetShelfListRes();
        getShelfListRes.setShelfCnt(3);
        getShelfListRes.setShelfList(shelfList);
        return getShelfListRes;
    }

    public GetTotalShelfRes getTotalShelf(int sequence, int userId) {
        String query ="select book.id as book_id, title, author, file, image\n" +
                "from book inner join history_books on book.id = history_books.book_id ";

        switch (sequence){
            //최근 담은 순
            case 1: query = query + "where user_id = ? order by history_books.created_at;";
                break;
            //제목
            case 2: query = query + "where user_id = ? order by binary title;";
                break;
            // 작가
            case 3: query = query + "where user_id = ? order by binary author;";
                break;
            case 4: query = query + "join book_info on book.id = book_info.book_id " +
                    "where user_id = 3 order by paper_date desc;";
                break;
            case 5: query = query + "left join (select book_id, max(created_at) as recent from history_log group by book_id) l on l.book_id = history_books.book_id " +
                    "where history_books.user_id = ? order by l.recent desc";
                break;
            case 6: query = query + "where user_id = ? order by binary publisher;";
                break;
        }
        GetTotalShelfRes getTotalShelfRes = new GetTotalShelfRes();
        getTotalShelfRes.setBookCnt(this.jdbcTemplate.queryForObject("select count(title) as book_cnt\n" +
                "from book\n" +
                "         inner join history_books on book.id = history_books.book_id\n" +
                "                     where history_books.user_id = ?", int.class, userId));
        getTotalShelfRes.setBooks(this.jdbcTemplate.queryForList(query, userId));
        return getTotalShelfRes;
    }

    public GetShelfBooksRes getShelfBooks(int shelfId, int userIdxByJwt) {
        String query = "select shelf.name as name, shelf_books.book_id as book_id, title, author, image, concat(percentage, \"%\") as percent,\n" +
                "       if(datediff(now(), shelf_books.created_at) > 30, '만료', datediff(now(), shelf_books.created_at)) as exist_day from shelf_books\n" +
                "    join shelf on shelf.id = shelf_books.shelf_id join history_books on history_books.book_id = shelf_books.book_id\n" +
                "    join book on book.id = shelf_books.book_id\n" +
                "where shelf.id = ? and history_books.user_id = ? and shelf_books.status = 'Y'";
        GetShelfBooksRes getShelfBooksRes = new GetShelfBooksRes();
        getShelfBooksRes.setShfName(this.jdbcTemplate.queryForObject("select name from shelf where shelf.id = ?;"
                                    , String.class, shelfId));
        getShelfBooksRes.setBookCnt(this.jdbcTemplate.queryForObject("select count(*) as book_cnt from shelf_books\n" +
                        "    join shelf on shelf.id = shelf_books.shelf_id join history_books on history_books.book_id = shelf_books.book_id\n" +
                        "    join book on book.id = shelf_books.book_id\n" +
                        "where shelf.id = ? and history_books.user_id = ? and shelf_books.status = 'Y'"
                , int.class, new Object[]{shelfId,userIdxByJwt}));
        getShelfBooksRes.setBooks(this.jdbcTemplate.queryForList(query, new Object[]{shelfId,userIdxByJwt}));
        return getShelfBooksRes;
    }

    public int createShelf(PostShelfReq postShelfReq){
        this.jdbcTemplate.update("insert into shelf (name, user_id) values (?,?);",
                new Object[]{postShelfReq.getName(), postShelfReq.getUserId()});
        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

    public String createShfBook(PostShfBookReq postShfBookReq){

        for(int i = 0; i < postShfBookReq.getBookId().length; i++){
            this.jdbcTemplate.update("insert into shelf_books (shelf_id, book_id) values (?,?);",
                    new Object[]{postShfBookReq.getShelfId(), postShfBookReq.getBookId()[i]});
        }
        return postShfBookReq.getBookId().length+"권 추가 되었습니다.";
    }
    public String deleteShfBook(PatchShelfReq postShfBookReq){
        for (int i = 0; i< postShfBookReq.getBookId().length; i++ ){
            this.jdbcTemplate.update("update shelf_books set status='N' where book_id = ? and shelf_id = ?;"
                    , postShfBookReq.getBookId()[i], postShfBookReq.getShelfId());
        }
        return postShfBookReq.getBookId().length+"권 삭제 되었습니다.";
    }

    public int checkShfBook(PostShfBookReq postShfBookReq){
        for(int i =0; i<postShfBookReq.getBookId().length; i++ ){
            int exist = this.jdbcTemplate.queryForObject("select exists(select book_id from shelf_books where book_id = ? " +
                    "and shelf_id = ? and status= 'Y');", int.class, new Object[]{postShfBookReq.getBookId()[i], postShfBookReq.getShelfId()});
            if (exist == 1){
                return 1;
            }
        }
        return 0;
    }

    public int checkPtjBooh(PatchShelfReq postShfBookReq) {
        for (int i = 0; i < postShfBookReq.getBookId().length; i++) {
            int exist = this.jdbcTemplate.queryForObject("select exists(select book_id from shelf_books where book_id = ? " +
                    "and shelf_id = ? and status= 'Y');", int.class, new Object[]{postShfBookReq.getBookId()[i], postShfBookReq.getShelfId()});
            if (exist == 1) {
                return 0;
            }
        }
        return 1;
    }
    public int checkShfId(int shelfId){
        return this.jdbcTemplate.queryForObject("select exists(select shelf.id from shelf where shelf.id = ?);",
                int.class, shelfId);
    }

    public int checkUserShf(PostShfBookReq postShfBookReq,int userIdJwt){
        return this.jdbcTemplate.queryForObject("select exists(select shelf.id from shelf where shelf.id =? and" +
                " user_id = ?)", int.class, new Object[]{postShfBookReq.getShelfId(), userIdJwt});
    }
}
