/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.R;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.Collections;

public class CloudConsentProvider {

    final static String NEURA = "neura";

    private ConsentHandlerInterface consentHandler;

    public CloudConsentProvider(ConsentHandlerInterface consentHandler) {
        this.consentHandler = consentHandler;
    }

    public static ConsentDefinition getCloudConsentDefinition() {
        int text = R.string.ail_cloud_consent_title;
        int helpText = R.string.ail_cloud_consent_help;
        final ArrayList<String> types = new ArrayList<>();
        types.add(NEURA);
        return new ConsentDefinition(text, helpText, types, BuildConfig.VERSION_CODE);
    }

    public void registerConsentHandler(AppInfraInterface appInfraInterface) {
        appInfraInterface.getConsentManager().registerHandler(Collections.singletonList(NEURA), consentHandler);
    }

    public void fetchConsentHandler(FetchConsentTypeStateCallback fetchConsentTypeStateCallback) {
        consentHandler.fetchConsentTypeState(NEURA, fetchConsentTypeStateCallback);
    }

    void storeConsentTypeState(boolean state, PostConsentTypeCallback postConsentTypeCallback) {
        consentHandler.storeConsentTypeState(NEURA, state, BuildConfig.VERSION_CODE, postConsentTypeCallback);
    }
}
