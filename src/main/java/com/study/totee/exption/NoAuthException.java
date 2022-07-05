package com.study.totee.exption;

public class NoAuthException extends RuntimeException{
    public NoAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}