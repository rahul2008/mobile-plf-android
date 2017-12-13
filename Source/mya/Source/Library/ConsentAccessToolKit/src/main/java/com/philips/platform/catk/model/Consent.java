package com.philips.platform.catk.model;


import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;

public class Consent {

    private ConsentDefinition definition;
    private List<BackendConsent> consents;

    public Consent(BackendConsent consent, @NonNull ConsentDefinition definition) {
        this.consents = new ArrayList<>();
        this.consents.add(consent);
        this.definition = definition;
    }

    public Consent(List<BackendConsent> consent, @NonNull ConsentDefinition definition) {
        this.consents = consent;
        this.definition = definition;
    }

    public boolean isChangeable() {
        BackendConsent consent = getRepresentingConsent();
        return consent == null || definition.getVersion() >= consent.getVersion();
    }

    public boolean isAccepted() {
        BackendConsent consent = getRepresentingConsent();
        return consent != null && consent.getStatus().equals(ConsentStatus.active) && definition.getVersion() <= consent.getVersion();
    }

    public String getType() {
        BackendConsent consent = getRepresentingConsent();
        return consent != null ? consent.getType() : null;
    }

    public ConsentDefinition getDefinition() { return definition; }

    private BackendConsent getRepresentingConsent() {
        return (consents != null) && (consents.size() > 0) ? consents.get(0) : null;
    }
}
