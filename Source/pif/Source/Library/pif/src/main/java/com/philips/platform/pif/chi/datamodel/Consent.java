package com.philips.platform.pif.chi.datamodel;


import java.util.ArrayList;
import java.util.List;

public class Consent {

    private ConsentDefinition definition;
    private List<BackendConsent> consents;

    public Consent(BackendConsent consent, ConsentDefinition definition) {
        this.consents = new ArrayList<>();
        this.consents.add(consent);
        this.definition = definition;
    }

    public Consent(List<BackendConsent> consent, ConsentDefinition definition) {
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

    public ConsentStates getStatus() {
        BackendConsent consent = getRepresentingConsent();
        return consent != null ? consent.getStatus() : ConsentStates.inactive;
    }

    public ConsentDefinition getDefinition() {
        return definition;
    }

    private BackendConsent getRepresentingConsent() {
        return (consents != null) && (consents.size() > 0) ? consents.get(0) : null;
    }

    private boolean isConsentActive(BackendConsent consent) {
        return consent.getStatus().equals(ConsentStates.active) && definition.getVersion() <= consent.getVersion();
    }
}
