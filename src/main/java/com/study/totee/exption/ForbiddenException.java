package com.study.totee.exption;


public class ForbiddenException extends RuntimeException{
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
