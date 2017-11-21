package com.philips.platform.csw;

import android.os.Bundle;
import android.os.Parcelable;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.model.ConsentDefinition;

import java.util.ArrayList;
import java.util.List;

public class ConsentBundleConfig {
    private String applicationName;
    private String propositionName;
    private List<ConsentDefinition> consentDefinitions;


    public ConsentBundleConfig(String applicationName, String propositionName, List<ConsentDefinition> consentDefinitions) {
        this.applicationName = applicationName;
        this.propositionName = propositionName;
        this.consentDefinitions = consentDefinitions;
    }

    public ConsentBundleConfig(Bundle bundle) {
        if(bundle!=null) {
            this.applicationName = bundle.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            this.propositionName = bundle.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
            this.consentDefinitions = bundle.getParcelableArrayList(CatkConstants.BUNDLE_KEY_CONSENT_DEFINITIONS);
        }
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putParcelableArrayList(CatkConstants.BUNDLE_KEY_CONSENT_DEFINITIONS, new ArrayList<Parcelable>(consentDefinitions));
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        return b;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }

    public List<ConsentDefinition> getConsentDefinitions() {
        return consentDefinitions;
    }
}
