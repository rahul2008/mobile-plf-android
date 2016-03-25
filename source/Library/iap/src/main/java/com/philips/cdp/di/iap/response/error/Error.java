package com.philips.cdp.di.iap.response.error;

public class Error {
    private String message;
    private String reason;
    private String subject;
    private String subjectType;
    private String type;

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public String getSubject() {
        return subject;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public String getType() {
        return type;
    }
}
