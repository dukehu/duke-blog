package com.duke.microservice.blog.common;

import java.io.Serializable;

/**
 * Created duke on 2018/6/23
 */
public class Response<T> implements Serializable {
    private Integer status;
    private String message;
    private T data;

    public Response() {
    }

    private Response(T data) {
        this.status = 200;
        this.message = "成功";
        this.data = data;
    }

    public static <T> Response<T> ok(T data) {
        return new Response(data);
    }

    public static <String> Response<String> ok() {
        return new Response("success");
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
