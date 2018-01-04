package com.philips.platform.catk.model;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
        if (consents == null || consents.size() == 0) {
            return false;
        }
        for (BackendConsent consent : consents) {
            if (consent == null || !isConsentActive(consent)) {
                return false;
            }
        }
        return true;
    }

    public String getType() {
        BackendConsent consent = getRepresentingConsent();
        return consent != null ? consent.getType() : null;
    }

    public ConsentStatus getStatus(){
        BackendConsent consent = getRepresentingConsent();
        return consent != null ? consent.getStatus() : ConsentStatus.inactive;
    }

    public ConsentDefinition getDefinition() {
        return definition;
    }

    private BackendConsent getRepresentingConsent() {
        return (consents != null) && (consents.size() > 0) ? consents.get(0) : null;
    }

    private boolean isConsentActive(BackendConsent consent) {
        return consent.getStatus().equals(ConsentStatus.active) && definition.getVersion() <= consent.getVersion();
    }
}
