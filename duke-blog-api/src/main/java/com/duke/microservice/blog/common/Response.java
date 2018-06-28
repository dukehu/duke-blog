package com.duke.microservice.blog.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created duke on 2018/6/23
 */
public class Response<T> implements Serializable {
    private Integer status;
    private String code;
    private String message;
    private T data;
    private List<FieldError> fieldErrors;

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

    public Response(List<FieldError> fieldErrors) {
        this.status = 10000;
        this.code = "data_validate_exception";
        this.message = "数据校验失败";
        this.fieldErrors = fieldErrors;
    }

    public static Response error(Integer status, String code, String mssage) {
        return new Response(status, code, mssage);
    }

    public static Response error(List<FieldError> fieldErrors) {
        return new Response(fieldErrors);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
