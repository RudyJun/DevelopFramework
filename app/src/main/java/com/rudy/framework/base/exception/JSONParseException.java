package com.rudy.framework.base.exception;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class JSONParseException extends Exception {

    public JSONParseException(String detailMessage){
        super(detailMessage);
    }

    public JSONParseException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
