/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.Nullable;

import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

public class ConsentView {

    private final ConsentDefinition definition;
    private final ConsentHandlerInterface handler;
    private boolean isLoading = true;
    private boolean isError = false;
    private boolean isOnline = true;

    @Nullable
    private Consent consent;

    ConsentView(final ConsentDefinition definition, final ConsentHandlerInterface handler) {
        this.definition = definition;
        this.handler = handler;
    }

    public String getConsentText() {
        return definition.getText();
    }

    public String getHelpText() {
        return definition.getHelpText();
    }

    public String getType() {
        return definition.getTypes().get(0);
    }

    public int getVersion() {
        return definition.getVersion();
    }

    public ConsentHandlerInterface getHandler() {
        return handler;
    }

    public ConsentView storeConsent(Consent consent) {
        this.consent = consent;
        this.isLoading = false;
        this.isError = false;
        this.isOnline = true;
        return this;
    }

    public void setError(boolean isError) {
        this.isLoading = false;
        this.isError = isError;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isEnabled() {
        return (consent == null || consent.isChangeable()) && !isError;
    }

    public boolean isChecked() {
        return consent != null && consent.isAccepted();
    }

    public boolean isError() {
        return isError;
    }

    public ConsentDefinition getDefinition() {
        return definition;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setNotFound() {
        isLoading = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ConsentView that = (ConsentView) o;

        if (!definition.equals(that.definition))
            return false;
        return consent != null ? consent.equals(that.consent) : that.consent == null;
    }

    @Override
    public int hashCode() {
        int result = definition.hashCode();
        result = 31 * result + (consent != null ? consent.hashCode() : 0);
        return result;
    }
}
