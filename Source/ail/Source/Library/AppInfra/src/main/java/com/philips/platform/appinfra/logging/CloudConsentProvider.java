/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.Collections;

public class CloudConsentProvider {

    public final static String CLOUD = "cloud";

    private ConsentHandlerInterface consentHandler;

    public CloudConsentProvider(ConsentHandlerInterface consentHandler) {
        this.consentHandler = consentHandler;
    }

    public static ConsentDefinition getCloudConsentDefinition() {
        int text = R.string.ail_cloud_consent_title;
        int helpText = R.string.ail_cloud_consent_help;
        final ArrayList<String> types = new ArrayList<>();
        types.add(CLOUD);
        return new ConsentDefinition(text, helpText, types, 1);
    }

    public void registerConsentHandler(ConsentManagerInterface consentManagerInterface) {
        consentManagerInterface.registerHandler(Collections.singletonList(CLOUD), consentHandler);
    }

    private void fetchConsentHandler(FetchConsentTypeStateCallback fetchConsentTypeStateCallback) {
        consentHandler.fetchConsentTypeState(CLOUD, fetchConsentTypeStateCallback);
    }

    public void storeConsentTypeState(boolean state, PostConsentTypeCallback postConsentTypeCallback) {
        consentHandler.storeConsentTypeState(CLOUD, state, BuildConfig.VERSION_CODE, postConsentTypeCallback);
    }

    public boolean isCloudLoggingConsentProvided() {
        final boolean[] status = new boolean[1];
        fetchConsentHandler(new FetchConsentTypeStateCallback() {
            @Override
            public void onGetConsentsSuccess(ConsentStatus consentStatus) {
                status[0] = consentStatus.getConsentState() == ConsentStates.active;
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                status[0] = false;
            }
        });
        return status[0];
    }
}
