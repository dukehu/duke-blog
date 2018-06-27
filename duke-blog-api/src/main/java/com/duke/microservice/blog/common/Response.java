package com.duke.microservice.blog.common;

import java.io.Serializable;

/**
 * Created duke on 2018/6/23
 */
public class Response<T> implements Serializable {
    private Integer status;
    private String code;
    private String message;
    private T data;

    public Response() {
    }

    private Response(T data) {
        this.status = 200;
        this.message = "成功";
        this.code = "ok";
        this.data = data;
    }

    public Response(Integer status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static Response error(Integer status, String code, String message) {
        return new Response(status, code, message);
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
