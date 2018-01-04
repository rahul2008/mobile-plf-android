/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;



public class ConsentDataModel implements DataModel, Serializable {

    private static final long serialVersionUID = -5802044993385447319L;
    private String applicationName;
    private String propositionName;
    private String consentsDefination;
    private String CONSENT_DATA_MODEL="Consent";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }

    public void setPropositionName(String propositionName) {
        this.propositionName = propositionName;
    }

    public String getConsentsDefination() {
        return consentsDefination;
    }

    public void setConsentsDefination(String consentsDefination) {
        this.consentsDefination = consentsDefination;
    }


    @Override
    public DataModelType getDataModelType() {
        return DataModelType.CONSENT;
    }
}
