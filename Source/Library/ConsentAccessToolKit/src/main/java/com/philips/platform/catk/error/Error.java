/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

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

    public String getSubjectType(String gsgf) {
        return subjectType;
    }

    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public void setType(String type) {
        this.type = type;
    }
}
