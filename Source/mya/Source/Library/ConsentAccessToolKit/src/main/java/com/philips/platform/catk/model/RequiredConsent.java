package com.philips.platform.catk.model;


import org.joda.time.DateTime;

import java.util.Locale;

public class RequiredConsent {

    private ConsentDefinition definition;
    private Consent consent;

    public RequiredConsent(Consent consent, ConsentDefinition definition) {
        this.consent = consent;
        this.definition = definition;
    }

    public boolean isChangeable() {
        return consent == null || definition.getVersion() >= consent.getVersion();
    }

    public boolean isAccepted() {
        return consent != null && consent.getStatus().equals(ConsentStatus.active) && definition.getVersion() <= consent.getVersion();
    }

    public Consent getConsent() { return consent; }

    public ConsentDefinition getDefinition() { return definition; }

    public Locale getLocale() {
        return consent == null ? null : consent.getLocale();
    }

    public ConsentStatus getStatus() {
        return consent == null ? null : consent.getStatus();
    }

    public String getType() {
        return consent == null ? null : consent.getType();
    }

    public int getVersion() {
        return consent == null ? null : consent.getVersion();
    }

    public DateTime getTimestamp() { return consent == null ? null : consent.getTimestamp(); }

}
