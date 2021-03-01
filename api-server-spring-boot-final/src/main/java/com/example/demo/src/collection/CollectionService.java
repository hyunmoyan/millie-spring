package com.example.demo.src.collection;



import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Service
public class CollectionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CollectionDao collectionDao;
    private final CollectionProvider collectionProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public CollectionService(CollectionDao collectionDao, CollectionProvider collectionProvider, JwtService jwtService) {
        this.collectionDao = collectionDao;
        this.collectionProvider = collectionProvider;
        this.jwtService = jwtService;

    }
}
