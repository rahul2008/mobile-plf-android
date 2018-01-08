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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to provide input parameters and customizations for Consent access tool kit.
 */

public class CatkInputs {

    private final AppInfraInterface appInfra;

    private final Context context;

    private final List<ConsentDefinition> consentDefinitionList;

    private CatkInputs(Builder builder) {
        appInfra = builder.appInfra;
        context = builder.context;
        consentDefinitionList = builder.consentDefinitionList;
    }

    public Context getContext() {
        return context;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public List<ConsentDefinition> getConsentDefinitions() {
        return Collections.unmodifiableList(consentDefinitionList);
    }

    public static class Builder {

        private AppInfraInterface appInfra;

        private Context context;

        private List<ConsentDefinition> consentDefinitionList;

        public Builder() {
        }

        public Builder setAppInfraInterface(AppInfraInterface appInfra) {
            this.appInfra = appInfra;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setConsentDefinitions(List<ConsentDefinition> consentDefinitions) {
            this.consentDefinitionList = consentDefinitions;
            return this;
        }

        public CatkInputs build() {
            if (appInfra == null) {
                throw new InvalidInputException("AppInfraInterface not given, we need:\n-AppInfra\n-Context\n-ConsentDefinitions");
            }
            if (context == null) {
                throw new InvalidInputException("Context not given, we need:\n-AppInfra\n-Context\n-ConsentDefinitions");
            }
            if (consentDefinitionList == null) {
                throw new InvalidInputException("ConsentDefinitions not given, we need:\n-AppInfra\n-Context\n-ConsentDefinitions");
            }

            final List<String> types = new ArrayList<>();
            for(ConsentDefinition definition : consentDefinitionList){
                for(String type : definition.getTypes()){
                    if(types.contains(type)) throw new InvalidInputException("Not allowed to have duplicate types in your Definitions, type:" + type + " occurs in multiple times");
                    types.add(type);
                }
            }

            return new CatkInputs(this);
        }
    }

    public static class InvalidInputException extends RuntimeException {

        public InvalidInputException(String message) {
            super(message);
        }
    }

}
