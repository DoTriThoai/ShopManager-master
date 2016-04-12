package com.trithoai.shopapp.models;

/**
 * Created by johncarter on 11/04/2016.
 */
public class ErrorMessage {
    private int errorCode;
    private String content;

    public ErrorMessage(int errorCode, String content) {
        this.errorCode = errorCode;
        this.content = content;
    }

    public ErrorMessage() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
