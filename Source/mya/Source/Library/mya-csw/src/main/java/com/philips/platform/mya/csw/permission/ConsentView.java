/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import static com.philips.platform.pif.chi.datamodel.ConsentStates.active;
import static com.philips.platform.pif.chi.datamodel.ConsentVersionStates.AppVersionIsLower;

public class ConsentView {

    private final ConsentDefinition definition;
    private boolean isLoading = true;
    private boolean isError = false;
    private boolean isOnline = true;
    private ConsentDefinitionStatus consentDefinitionStatus;

    ConsentView(final ConsentDefinition definition) {
        this.definition = definition;
    }

    public int getConsentText() {
        return definition.getText();
    }

    public int getHelpText() {
        return definition.getHelpText();
    }

    public String getType() {
        return definition.getTypes().get(0);
    }

    public int getVersion() {
        return definition.getVersion();
    }

    public ConsentView storeConsentDefnitionStatus(ConsentDefinitionStatus consentDefinitionStatus) {
        this.consentDefinitionStatus = consentDefinitionStatus;
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
        return (consentDefinitionStatus == null) || (!consentDefinitionStatus.getConsentVersionState().equals(AppVersionIsLower) && !isError);
    }

    public boolean isChecked() {
        return consentDefinitionStatus != null && consentDefinitionStatus.getConsentState().equals(active) && !consentDefinitionStatus.getConsentVersionState().equals(AppVersionIsLower);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentView that = (ConsentView) o;

        if (isLoading != that.isLoading) return false;
        if (isError != that.isError) return false;
        if (isOnline != that.isOnline) return false;
        if (!definition.equals(that.definition)) return false;
        return consentDefinitionStatus.equals(that.consentDefinitionStatus);
    }

    @Override
    public int hashCode() {
        int result = definition.hashCode();
        result = 31 * result + (isLoading ? 1 : 0);
        result = 31 * result + (isError ? 1 : 0);
        result = 31 * result + (isOnline ? 1 : 0);
        result = 31 * result + consentDefinitionStatus.hashCode();
        return result;
    }
}
