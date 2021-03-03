package com.example.demo.src.collection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.collection.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.COLLECTION_SUCCESS;

@RestController
@RequestMapping("/collections")
public class CollectionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CollectionProvider collectionProvider;
    @Autowired
    private final CollectionService collectionService;
    @Autowired
    private final JwtService jwtService;




    public CollectionController(CollectionProvider collectionProvider, CollectionService collectionService, JwtService jwtService){
        this.collectionProvider = collectionProvider;
        this.collectionService = collectionService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetCollectionRes>> getCollections() {
        // Get Users
        List<GetCollectionRes> getCollectionsRes = collectionProvider.getCollections();
        return new BaseResponse<>(getCollectionsRes);
    }

//    특정 책 컬렉션 조 api [get] /books/{collection_id}
    @ResponseBody
    @GetMapping("/{collectionId}") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<GetCollectionRes> getCollection(@PathVariable("collectionId") int collectionId) {
        // Get Users
        GetCollectionRes getCollectionRes = collectionProvider.getCollection(collectionId);
        return new BaseResponse<>(getCollectionRes);
    }
}
