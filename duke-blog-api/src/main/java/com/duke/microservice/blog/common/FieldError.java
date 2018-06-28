package com.duke.microservice.blog.common;

/**
 * Created duke on 2018/6/28
 */
public class FieldError {

    String filedName;

    String errorMsg;

    public FieldError() {
    }

    public FieldError(String filedName, String errorMsg) {
        this.filedName = filedName;
        this.errorMsg = errorMsg;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return this.filedName + ":" + this.errorMsg;
    }
}
