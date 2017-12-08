/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import java.util.ArrayList;
import java.util.List;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.model.ConsentDefinition;

import android.os.Bundle;
import android.os.Parcelable;

public class ConsentBundleConfig {
    private final List<ConsentDefinition> consentDefinitions;

    public ConsentBundleConfig(List<ConsentDefinition> consentDefinitions) {
        this.consentDefinitions = consentDefinitions == null ? new ArrayList<ConsentDefinition>() : consentDefinitions;
    }

    public ConsentBundleConfig(Bundle bundle) {
        if (bundle != null) {
            this.consentDefinitions = bundle.getParcelableArrayList(CatkConstants.BUNDLE_KEY_CONSENT_DEFINITIONS);
        } else {
            this.consentDefinitions = new ArrayList<>();
        }
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putParcelableArrayList(CatkConstants.BUNDLE_KEY_CONSENT_DEFINITIONS, new ArrayList<Parcelable>(consentDefinitions));
        return b;
    }

    public List<ConsentDefinition> getConsentDefinitions() {
        return consentDefinitions;
    }
}
