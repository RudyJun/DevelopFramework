package com.rudy.framework.base.exception;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class CommonException extends Exception {
    public CommonException(String detailMessage){
        super(detailMessage);
    }

    public CommonException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
