package com.study.totee.exption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_EXIST_USER_ERROR("존재하지 않는 유저입니다."),
    NOT_EXIST_CATEGORY_ERROR("존재하지 않는 카테고리입니다."),
    NO_AUTHENTICATION_ERROR("로그인이 필요한 서비스입니다."),
    INVALID_POSITION_TYPE_ERROR("존재하지 않는 Position Type 입니다."),
    INVALID_ROLE_TYPE_ERROR("존재하지 않는 Role Type 입니다."),
    NO_POST_ERROR("존재하지 않는 게시물입니다."),
    NO_COMMENT_ERROR("존재하지 않는 댓글입니다."),
    NO_REPLY_ERROR("존재하지 않는 답글입니다."),
    BAD_REQUEST_ERROR("잘못된 요청입니다."),
    BAD_KEYWORD_ERROR("검색할 키워드는 1자 이상이어야 합니다."),
    NO_USER_INFO_ERROR("존재하지 않는 유저정보입니다."),
    NO_ROLE_ERROR("존재하지 않는 권한입니다."),
    NO_AUTHORITY_ERROR("권한이 없습니다."),
    ALREADY_EXIST_ERROR("이미 존재하는 유저입니다."),
    ALREADY_EXIST_NICKNAME_ERROR("이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_MENTOR_APPLY("이미 멘토 등록 진행 중입니다."),
    ALREADY_EXIST_CATEGORY_ERROR("이미 존재하는 카테고리입니다."),
    IMAGE_SAVE_ERROR("이미지 저장 실패"),
    INVALID_INPUT_ERROR("잘못된 입력입니다."),
    NO_NOTIFICATION_ERROR("존재하지 않는 알림입니다."),
    NO_APPLICANT_ERROR("존재하지 않는 지원자입니다."),
    ALREADY_APPLY_POST_ERROR("이미 지원하였습니다."),
    NO_AUTHORIZATION_ERROR("접근 권한이 없습니다."),
    ALREADY_STARTED_ERROR("모집이 완료된 프로젝트입니다."),
    ALREADY_TEAM_ERROR("이미 팀원에 속해 있습니다."),
    NOT_AVAILABLE_ACCESS("잘못된 접근입니다."),
    NO_TEAM_ERROR("팀원이 아닌 사용자입니다."),
    NO_EXCEED_MEMBER_ERROR("더이상 멤버를 받을 수 없습니다");
    private String message;
}
