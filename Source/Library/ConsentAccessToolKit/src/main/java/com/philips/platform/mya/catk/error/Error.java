package com.philips.platform.mya.catk.error;

/**
 * Created by Maqsood on 10/12/17.
 */

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
