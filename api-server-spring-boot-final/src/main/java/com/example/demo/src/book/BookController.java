package com.example.demo.src.book;


import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.book.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/books")
public class BookController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BookProvider bookProvider;
    @Autowired
    private final BookService bookService;
    @Autowired
    private final JwtService jwtService;




    public BookController(BookProvider bookProvider, BookService bookService, JwtService jwtService){
        this.bookProvider = bookProvider;
        this.bookService = bookService;
        this.jwtService = jwtService;
    }


//    특정 책 컬렉션 조 api [get] /books/{collection_id}
    @ResponseBody
    @GetMapping("/{collectionId}") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<GetBookRes> getBooks(@PathVariable("collectionId") int collectionId) {
        // Get Users
        GetBookRes getBooksRes = bookProvider.getBooks(collectionId);
        return new BaseResponse<>(getBooksRes);
    }
}
