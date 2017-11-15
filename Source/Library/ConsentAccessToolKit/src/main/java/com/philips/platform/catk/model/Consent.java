/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.model;

import com.philips.platform.catk.response.ConsentStatus;

public class Consent {
    private String locale;
    private ConsentStatus status;
    private String type;
    private int version;

    public Consent(String locale, ConsentStatus status, String type, int version) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consent)) return false;

        Consent consent = (Consent) o;

        if (version != consent.version) return false;
        if (locale != null ? !locale.equals(consent.locale) : consent.locale != null) return false;
        if (status != consent.status) return false;
        return type != null ? type.equals(consent.type) : consent.type == null;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
