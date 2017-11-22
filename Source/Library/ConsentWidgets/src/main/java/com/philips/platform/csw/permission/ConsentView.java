/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.support.annotation.Nullable;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.model.RequiredConsent;

public class ConsentView {

    private final ConsentDefinition definition;
    private boolean isLoading = true;
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
        return this;
    }

    boolean isEnabled() {
        return consent != null && consent.isChangeable();
    }

    boolean isChecked() {
        return consent !=null && consent.isAccepted();
    }

    ConsentDefinition getDefinition() {
        return definition;
    }

    boolean isLoading() {
        return consent == null || consent.getConsent() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentView that = (ConsentView) o;

        if (!definition.equals(that.definition)) return false;
        return consent != null ? consent.equals(that.consent) : that.consent == null;
    }

    @Override
    public int hashCode() {
        int result = definition.hashCode();
        result = 31 * result + (consent != null ? consent.hashCode() : 0);
        return result;
    }
}
