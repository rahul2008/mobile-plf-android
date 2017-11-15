package com.philips.platform.catk.model;


public class Consent {

    private String locale;

    private String status;

    private String type;

    private int version;

    public Consent(String locale, String status, String type, int version) {
        this.locale = locale;
        this.status = status;
        this.type = type;
        this.version = version;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
