package com.xmtj.wtf.gather.mvp.model;

import java.io.Serializable;

public class BaseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public int errorCode;
    public String message;
    public T data;
    public Page CurrentPage;

    @Override
    public String toString() {
        return "BaseData{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", CurrentPage=" + CurrentPage +
                '}';
    }
}