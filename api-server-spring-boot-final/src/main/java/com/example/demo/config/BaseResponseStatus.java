package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    COLLECTION_SUCCESS(true, 1001, "컬렉션을 가져오는데 성공했습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    // [POST] /books
    POST_BOOKS_EMPTY_TITLE(false, 2020, "책 제목 혹은 작가 비었습니다."),
    POST_BOOKS_INVALID_NUMBER(false, 2021, "유효하지 않은 번호입니다."),
    POST_BOOKS_EXITS_TITLE(false, 2022, "이미 있는 책입니다."),

    // /shelfs
    GET_SHELFS_INVAILD_NUMBER(false, 2030, "shelf: 유효하지 않은 번호입니다."),
    POST_SHELFS_EMPTY(false, 2031, "shelf: 제목이 비었습니다."),
    SHELFS_ID_EMPTY(false, 2032, "shelf: 아이디 값을 확해주세요."),
    POST_CHELFS_EXISTS(false, 2033, "책이 책장에 이미 존재합니다."),
    PATCH_SHELFS_ALREADY_DELETED(false, 2040, "shelf: 책장에 책이 존재하지 않습니다"),
    GET_SHELF_INVALID_ID(false, 2034, "shelf: 존재하지 않는 책장입니다."),
    POST_SHELFS_INVAILD_USER(false, 2035, "유저가 가진 책장이 아닙니다."),
    POST_BOOKS_INVALID(false, 2040, "추가할 수 없는 책 id 입니다. 읽은 목록에 없는 책입니다."),

    //posts
    POST_EMPTY_TITLE(false, 2100, "포스트 제목이 비었습니다."),
    POST_EMPTY_CONTENT(false, 2101, "포스트 내용이 비었습니다."),
    POST_NOT_EXIST(false, 2101, "포스트가 존재하지 않습니다."),
    POST_USER_DIFF(false, 2110, "유저의 포스트가 아니거나 삭제된 포스트 입니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
