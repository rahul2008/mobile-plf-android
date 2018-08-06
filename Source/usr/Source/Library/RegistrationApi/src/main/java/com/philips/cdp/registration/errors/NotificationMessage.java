package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.ui.customviews.NotificationType;

public class NotificationMessage {

    private String message;
    private String title;
    private int errorCode;
    private NotificationType notificationType;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationMessage(NotificationType notificationType, String message, int errorCode) {
        this.notificationType = notificationType;
        this.message = message;
        this.errorCode = errorCode;
    }

    public NotificationMessage(NotificationType notificationType, String message, String title) {
        this.notificationType = notificationType;
        this.message = message;
        this.title = title;
    }


    public NotificationMessage(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
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
