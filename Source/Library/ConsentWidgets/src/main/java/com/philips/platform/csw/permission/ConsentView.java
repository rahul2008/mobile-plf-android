/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.Nullable;

import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;

public class ConsentView {

    private final ConsentDefinition definition;
    private boolean isLoading = true;
    private boolean isError = false;

    @Nullable
    private RequiredConsent consent;

    ConsentView(final ConsentDefinition definition) {
        this.definition = definition;
    }

    public String getConsentText() {
        return definition.getText();
    }

    String getHelpText() {
        return definition.getHelpText();
    }

    public String getType() {
        return definition.getType();
    }

    public int getVersion() {
        return definition.getVersion();
    }

    ConsentView storeConsent(RequiredConsent consent) {
        this.consent = consent;
        this.isLoading = false;
        this.isError = false;
        return this;
    }

    void setError(boolean isError) {
        this.isLoading = false;
        this.isError = isError;
    }

    boolean isEnabled() {
        return consent != null && consent.isChangeable() && !isError;
    }

    boolean isChecked() {
        return consent != null && consent.isAccepted();
    }

    boolean isError() {
        return isError;
    }

    ConsentDefinition getDefinition() {
        return definition;
    }

    boolean isLoading() {
        return consent == null || consent.getConsent() == null;
    }

    void setNotFound() {
        isLoading = false;
    }

    void setIsLoading() {
        isLoading = true;
        isError = false;
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
