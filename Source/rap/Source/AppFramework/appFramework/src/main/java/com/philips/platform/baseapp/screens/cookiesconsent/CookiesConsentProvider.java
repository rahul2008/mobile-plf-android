/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.baseapp.screens.cookiesconsent;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.Collections;

import static com.philips.platform.appframework.flowmanager.AppStates.COOKIES_CONSENT;
import static com.philips.platform.appframework.flowmanager.AppStates.NEURA;

public class CookiesConsentProvider {


    private ConsentHandlerInterface consentHandler;

    public CookiesConsentProvider(ConsentHandlerInterface consentHandler) {
        this.consentHandler = consentHandler;
    }

    public static ConsentDefinition getAbTestingConsentDefinition() {
        int text = R.string.RA_neura_consent_title;
        int helpText = R.string.RA_neura_consent_help;
        final ArrayList<String> types = new ArrayList<>();
        types.add(COOKIES_CONSENT);
        return new ConsentDefinition(text, helpText, types, 1);
    }

    public void registerConsentHandler(AppInfraInterface appInfraInterface) {
        appInfraInterface.getConsentManager().registerHandler(Collections.singletonList(COOKIES_CONSENT), consentHandler);
    }

    public void fetchConsentHandler(FetchConsentTypeStateCallback fetchConsentTypeStateCallback) {
        consentHandler.fetchConsentTypeState(COOKIES_CONSENT, fetchConsentTypeStateCallback);
    }

    void storeConsentTypeState(boolean state, PostConsentTypeCallback postConsentTypeCallback) {
        consentHandler.storeConsentTypeState(COOKIES_CONSENT, state, BuildConfig.VERSION_CODE, postConsentTypeCallback);
    }
}
