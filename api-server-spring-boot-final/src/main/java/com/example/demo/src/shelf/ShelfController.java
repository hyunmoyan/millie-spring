package com.example.demo.src.shelf;

import com.example.demo.config.BaseException;
import com.example.demo.src.book.model.PostBookRes;
import com.example.demo.src.shelf.model.*;
import com.example.demo.src.user.model.PostUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/shelfs")
public class ShelfController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ShelfProvider shelfProvider;
    @Autowired
    private final ShelfService shelfService;
    @Autowired
    private final JwtService jwtService;

    public ShelfController(ShelfProvider shelfProvider, ShelfService shelfService, JwtService jwtService) {
        this.shelfProvider = shelfProvider;
        this.shelfService = shelfService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<GetTotalShelfRes> getShelfsBooks(@RequestParam(required = false, defaultValue = "1") String sequence) throws BaseException {
        int Intsequence = Integer.parseInt(sequence);
        if(Intsequence >= 7|| 1 > Intsequence){
            return new BaseResponse<>(GET_SHELFS_INVAILD_NUMBER);
        }
        int userId = jwtService.getUserIdx();
        GetTotalShelfRes getTotalShelfRes = shelfProvider.GetShelfsBookList(Intsequence);
        return new BaseResponse<>(getTotalShelfRes);
    }

    @ResponseBody
    @GetMapping("/{shelfId}")
    public BaseResponse<GetShelfBooksRes> getShelfBooks(@PathVariable("shelfId") int shelfIdx) throws BaseException{
        try {
            GetShelfBooksRes getShelfBooksRes = shelfProvider.getShelfBooks(shelfIdx);
            return new BaseResponse<>(getShelfBooksRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostShelfRes> PostShelf(@RequestBody PostShelfReq postShelfReq) throws BaseException {
        if (postShelfReq.getName() == null){
            return new BaseResponse(POST_SHELFS_EMPTY);
        }
        try {
            PostShelfRes postShelfRes = shelfService.createShelf(postShelfReq);
            return new BaseResponse<>(postShelfRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/books")
    public BaseResponse<PostShfBookRes> PostShfBook(@RequestBody PostShfBookReq postShfBookReq) throws BaseException {

        if (postShfBookReq.getShelfId() == 0 || postShfBookReq.getBookId().length == 0){
            return new BaseResponse(SHELFS_ID_EMPTY);
        }
        try{
            PostShfBookRes postShfBookRes = shelfService.createShfBook(postShfBookReq);
            return new BaseResponse<>(postShfBookRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/books")
    public BaseResponse<PostShfBookRes> PatchShfBook(@RequestBody PatchShelfReq postShfBookReq) throws BaseException {
        if (postShfBookReq.getShelfId() == 0 || postShfBookReq.getBookId().length == 0){
            return new BaseResponse(SHELFS_ID_EMPTY);
        }
        try{
            PostShfBookRes postShfBookRes = shelfService.deleteShfBook(postShfBookReq);
            return new BaseResponse<>(postShfBookRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
