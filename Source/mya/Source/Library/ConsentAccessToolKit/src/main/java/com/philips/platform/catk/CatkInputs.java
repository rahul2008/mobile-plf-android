/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

import java.util.List;

/**
 * This class is used to provide input parameters and customizations for BackendConsent access tool kit.
 */

public class CatkInputs {

    private AppInfraInterface appInfra;

    private Context context;

    private List<ConsentDefinition> consentDefinitionList;

    CatkInputs() {
    }

    void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    void setAppInfra(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    public List<ConsentDefinition> getConsentDefinitions() {
        return consentDefinitionList;
    }

    void setConsentDefinitionList(List<ConsentDefinition> consentDefinitionList) {
        this.consentDefinitionList = consentDefinitionList;
    }

    public static class Builder {

        private CatkInputs catkInputs;

        public Builder() {
            catkInputs = new CatkInputs();
        }

        public Builder setAppInfraInterface(AppInfraInterface appInfra) {
            catkInputs.setAppInfra(appInfra);
            return this;
        }

        public Builder setContext(Context context) {
            catkInputs.setContext(context);
            return this;
        }

        public Builder setConfigurations(List<ConsentDefinition> consentDefinitions) {
            catkInputs.setConsentDefinitionList(consentDefinitions);
            return this;
        }

        public CatkInputs build() {
            if (catkInputs.getConsentDefinitions() == null) {
                throw new InvalidInputException("consent configurations were not given");
            }
            return catkInputs;
        }
    }

    public static class InvalidInputException extends RuntimeException {

        public InvalidInputException(String message) {
            super(message);
        }
    }

}
