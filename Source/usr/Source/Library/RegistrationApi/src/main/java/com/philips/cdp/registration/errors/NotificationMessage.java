package com.philips.cdp.registration.errors;

public class NotificationMessage {

    private String message;
    private String title ;
    private int errorCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationMessage(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public NotificationMessage(String message, String title) {
        this.message = message;
        this.title = title;
    }


    public NotificationMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
