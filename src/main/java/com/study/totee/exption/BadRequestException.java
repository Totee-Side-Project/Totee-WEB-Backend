package com.study.totee.exption;

public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

}
