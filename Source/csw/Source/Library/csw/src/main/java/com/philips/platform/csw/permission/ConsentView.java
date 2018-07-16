/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.Date;

import static com.philips.platform.pif.chi.datamodel.ConsentStates.active;
import static com.philips.platform.pif.chi.datamodel.ConsentVersionStates.AppVersionIsLower;

public class ConsentView {

    private final ConsentDefinition definition;
    private boolean isLoading = true;
    private boolean isError = false;
    private boolean isOnline = true;

    private ConsentDefinitionStatus consentDefinitionStatus;
    private boolean isEnabled;

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
        this.isEnabled = (consentDefinitionStatus == null) || (!consentDefinitionStatus.getConsentVersionState().equals(AppVersionIsLower));
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
        return this.isEnabled;
    }

    public void setEnabledFlag(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isChecked() {
        return consentDefinitionStatus != null && consentDefinitionStatus.getConsentState().equals(active);
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

    public Date getTimestamp(){
        return consentDefinitionStatus.getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentView that = (ConsentView) o;

        if (isLoading != that.isLoading) return false;
        if (isError != that.isError) return false;
        if (isOnline != that.isOnline) return false;
        if (isEnabled != that.isEnabled) return false;
        if (!definition.equals(that.definition)) return false;
        return consentDefinitionStatus.equals(that.consentDefinitionStatus);
    }

    @Override
    public int hashCode() {
        int result = definition.hashCode();
        result = 31 * result + (isLoading ? 1 : 0);
        result = 31 * result + (isError ? 1 : 0);
        result = 31 * result + (isOnline ? 1 : 0);
        result = 31 * result + (isEnabled ? 1 : 0);
        result = 31 * result + consentDefinitionStatus.hashCode();
        return result;
    }
}
