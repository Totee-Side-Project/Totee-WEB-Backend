package com.study.totee.exption;

public class RestApiException extends RuntimeException{
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
