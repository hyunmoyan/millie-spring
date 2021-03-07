package com.example.demo.src.collection;


import com.example.demo.src.collection.model.GetCollectionRes;
import com.example.demo.src.collection.model.GetCollectionsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCollectionsRes> getCollections(){
        List<GetCollectionsRes> getCollectionsRes = new ArrayList<>();
        List<Integer> cnt = this.jdbcTemplate.queryForList("select id from collection where status='Y'", int.class);
        // 가장 많이 읽은
        GetCollectionsRes bestCollection = new GetCollectionsRes();
        bestCollection.setTitle("가장 많이 읽은 책");
        bestCollection.setBooks(this.jdbcTemplate.queryForList("select book.id as book_id, title, image, author, category from\n" +
                "(select book_id, count(created_at) as cnt from history_log where date(now()) = date(created_at) group by book_id)\n" +
                "cnt inner join book on book.id = cnt.book_id order by cnt desc limit 30;"));
        getCollectionsRes.add(bestCollection);

        // 지금 새로 들어온 책
        GetCollectionsRes newBookCollection = new GetCollectionsRes();
        newBookCollection.setTitle("지금 새로 들어온 책");
        newBookCollection.setBooks(this.jdbcTemplate.queryForList("select book_id, title, image, author from book\n" +
                "    inner join book_info on book_info.book_id = book.id\n" +
                "    order by book_info.created_at desc limit 30;"));
        getCollectionsRes.add(newBookCollection);

        for(int i = 0; i < cnt.size() ; i++) {
            GetCollectionsRes getCollectionRes = new GetCollectionsRes();
            getCollectionRes.setTitle(this.jdbcTemplate.queryForObject("select title from collection where collection.id = ?;", String.class, cnt.get(i)));
            getCollectionRes.setBooks(this.jdbcTemplate.queryForList("select collection.id as collection_id, book.id as book_id, book.title as title, author, image from book join collection_books on book.id = collection_books.book_id\n" +
                    "inner join collection on collection_books.collection_id = collection.id where collection.id = ?;", cnt.get(i)));
            getCollectionsRes.add(getCollectionRes);
        }
        return getCollectionsRes;
    }

    public GetCollectionRes getCollection(int collectionId){
        GetCollectionRes getCollectionRes = new GetCollectionRes();
        getCollectionRes.setBooks(this.jdbcTemplate.queryForList("select collection.id as collection_id, book.id as book_id, book.title as title, author, image from book join collection_books on book.id = collection_books.book_id\n" +
                "inner join collection on collection_books.collection_id = collection.id where collection.id = ?;", collectionId));
        getCollectionRes.setTitle(this.jdbcTemplate.queryForObject("select title from collection where collection.id = ?;", String.class, collectionId));
        getCollectionRes.setBookCnt(getCollectionRes.getBooks().toArray().length);
        return getCollectionRes;
    }
}
