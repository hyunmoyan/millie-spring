package com.example.demo.src.book;


import com.example.demo.src.user.model.GetUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBookRes>> getBooks(@RequestParam(required = false) String category) {
        // Get Users
        List<GetBookRes> getBooksRes = bookProvider.getBooks(category);
        return new BaseResponse<>(getBooksRes);
    }

}
