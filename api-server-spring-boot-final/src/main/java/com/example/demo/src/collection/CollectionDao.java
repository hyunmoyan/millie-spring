package com.example.demo.src.collection;


import com.example.demo.src.collection.model.GetCollectionRes;
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

    public List<GetCollectionRes> getCollections(){
        List<GetCollectionRes> getCollectionsRes =new ArrayList<>();
        int cnt = this.jdbcTemplate.queryForObject("select count(*) from collection;", Integer.class).intValue();
        for(int i = 1; i < cnt+1 ; i++) {
            GetCollectionRes getCollectionRes = new GetCollectionRes();
            getCollectionRes.setTitle(this.jdbcTemplate.queryForObject("select title from collection where collection.id = ?;", String.class, i));
            getCollectionRes.setList(this.jdbcTemplate.queryForList("select collection.id as collection_id, book.id as book_id, book.title as title, author, image from book join collection_books on book.id = collection_books.book_id\n" +
                    "inner join collection on collection_books.collection_id = collection.id where collection.id = ?;", i));
            getCollectionsRes.add(getCollectionRes);
        }
        return getCollectionsRes;
    }

    public GetCollectionRes getCollection(int collectionId){
        GetCollectionRes getCollectionRes = new GetCollectionRes();
        getCollectionRes.setList(this.jdbcTemplate.queryForList("select collection.id as collection_id, book.id as book_id, book.title as title, author, image from book join collection_books on book.id = collection_books.book_id\n" +
                "inner join collection on collection_books.collection_id = collection.id where collection.id = ?;", collectionId));
        getCollectionRes.setTitle(this.jdbcTemplate.queryForObject("select title from collection where collection.id = ?;", String.class, collectionId));
        return getCollectionRes;
    }
}
