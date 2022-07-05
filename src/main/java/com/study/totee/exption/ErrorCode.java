package com.study.totee.exption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NO_USER_ERROR("존재하지 않는 유저입니다."),
    NO_CATEGORY_ERROR("존재하지 않는 카테고리입니다."),
    NO_AUTHENTICATION_ERROR("로그인이 필요한 서비스입니다."),
    NO_POSITION_TYPE_ERROR("존재하지 않는 Position Type 입니다."),
    NO_ROLE_TYPE_ERROR("존재하지 않는 Role Type 입니다."),
    NO_POST_ERROR("존재하지 않는 게시물입니다."),
    NO_COMMENT_ERROR("존재하지 않는 댓글입니다."),
    BAD_REQUEST_ERROR("잘못된 요청입니다."),
    BAD_KEYWORD_ERROR("검색할 키워드는 1자 이상이어야 합니다."),
    NO_USER_INFO_ERROR("존재하지 않는 유저정보입니다."),
    NO_ROLE_ERROR("존재하지 않는 권한입니다."),
    NO_AUTHORITY_ERROR("권한이 없습니다."),
    ALREADY_EXIST_ERROR("이미 존재하는 유저입니다."),
    ALREADY_EXIST_NICKNAME_ERROR("이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_CATEGORY_ERROR("이미 존재하는 카테고리입니다."),
    IMAGE_SAVE_ERROR("이미지 저장 실패"),
    INVALID_INPUT_ERROR("잘못된 입력입니다."),
    ;
    private String message;
}
