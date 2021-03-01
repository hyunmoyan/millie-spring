package com.example.demo.src.collection;

import com.example.demo.src.collection.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class CollectionProvider {

    private final CollectionDao collectionDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public CollectionProvider(CollectionDao collectionDao, JwtService jwtService) {
        this.collectionDao = collectionDao;
        this.jwtService = jwtService;
    }

    public List<GetCollectionRes> getCollections(){
        List<GetCollectionRes> getCollectionsRes = collectionDao.getCollections();
        return getCollectionsRes;
    }
    public GetCollectionRes getCollection(int collectionId){
        GetCollectionRes getCollectionRes = collectionDao.getCollection(collectionId);
        return  getCollectionRes;
    }
}
