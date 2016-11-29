package com.rudy.framework.base.exception;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class NetworkDisconnectException extends Exception {
    public NetworkDisconnectException(String detailMessage){
        super(detailMessage);
    }

    public NetworkDisconnectException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
