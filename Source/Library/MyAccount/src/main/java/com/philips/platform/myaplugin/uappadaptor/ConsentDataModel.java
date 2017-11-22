package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;

/**
 * Created by philips on 11/16/17.
 */

public class ConsentDataModel implements DataModel, Serializable {
    String applicationName;
    String propositionName;
    String consentsDefination;
    String CONSENT_DATA_MODEL="Consent";

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
