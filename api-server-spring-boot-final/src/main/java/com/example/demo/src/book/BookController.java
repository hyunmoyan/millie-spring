package com.example.demo.src.book;


import com.example.demo.config.BaseException;
import com.example.demo.src.book.model.GetBookRes;
import com.example.demo.src.book.model.PostBookRes;
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
        if(Integer.parseInt(category) > 18|| 0 > Integer.parseInt(category)){
            return new BaseResponse<>(POST_BOOKS_INVALID_NUMBER);
        }
        List<GetBookRes> getBooksRes = bookProvider.getBooks(category);
        return new BaseResponse<>(getBooksRes);
    }

    @ResponseBody
    @GetMapping("{bookId}")
    public BaseResponse<GetBookInfoRes> getBook(@PathVariable int bookId) throws BaseException {
        try {
            GetBookInfoRes getBookInfoRes = bookProvider.getBookInfo(bookId);
            return new BaseResponse<>(getBookInfoRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBookRes> createBook(@RequestBody PostBookReq postBookReq) throws BaseException {
        if(postBookReq.getTitle() == null || postBookReq.getAuthor() == null){
            return new BaseResponse<>(POST_BOOKS_EMPTY_TITLE);
        }
        if(postBookReq.getCategory() >18|| 0 > postBookReq.getCategory()){
            return new BaseResponse<>(POST_BOOKS_INVALID_NUMBER);
        }
        try {
            PostBookRes postBookRes = bookService.createBook(postBookReq);
            return new BaseResponse<>(postBookRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
