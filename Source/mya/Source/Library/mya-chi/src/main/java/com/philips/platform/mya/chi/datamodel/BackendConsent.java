/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.chi.datamodel;

import org.joda.time.DateTime;

public class BackendConsent {
    private String locale;
    private ConsentStatus status;
    private String type;
    private int version;
    private DateTime timestamp;

    public BackendConsent(String locale, ConsentStatus status, String type, int version) {
        this.locale = locale;
        this.status = status;
        this.type = type;
        this.version = version;
    }

    public BackendConsent(String locale, ConsentStatus status, String type, int version, DateTime timestamp) {
        this(locale, status, type, version);
        this.timestamp = timestamp;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String  locale) {
        this.locale = locale;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsentStatus status) {
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

    public DateTime getTimestamp() { return timestamp; }

    public void setTimestamp(DateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackendConsent)) return false;

        BackendConsent consent = (BackendConsent) o;

        if (version != consent.version) return false;
        if (locale != null ? !locale.equals(consent.locale) : consent.locale != null) return false;
        if (status != consent.status) return false;
        if (type != null ? !type.equals(consent.type) : consent.type != null) return false;
        return timestamp != null ? timestamp.equals(consent.timestamp) : consent.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
