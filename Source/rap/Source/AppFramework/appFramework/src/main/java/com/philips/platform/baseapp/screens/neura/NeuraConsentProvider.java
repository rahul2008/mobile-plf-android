/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.baseapp.screens.neura;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.Collections;

public class NeuraConsentProvider {

    private final static String NEURA = "neura";

    public static ConsentDefinition getNeuraConsentDefinition() {
        int text = R.string.RA_neura_consent_title;
        int helpText = R.string.RA_neura_consent_help;
        final ArrayList<String> types = new ArrayList<>();
        types.add(NEURA);
        return new ConsentDefinition(text, helpText, types, BuildConfig.VERSION_CODE);
    }

    public void registerConsentHandler(AppInfraInterface appInfraInterface) {
        DeviceStoredConsentHandler deviceStoredConsentHandler = new DeviceStoredConsentHandler(appInfraInterface);
        appInfraInterface.getConsentManager().registerHandler(Collections.singletonList(NEURA), deviceStoredConsentHandler);
    }

    public void fetchConsentHandler(AppInfraInterface appInfraInterface, FetchConsentTypeStateCallback fetchConsentTypeStateCallback) {
        new DeviceStoredConsentHandler(appInfraInterface).fetchConsentTypeState(NEURA, fetchConsentTypeStateCallback);
    }

    public void storeConsentTypeState(AppInfraInterface appInfraInterface, boolean state, PostConsentTypeCallback postConsentTypeCallback) {
        new DeviceStoredConsentHandler(appInfraInterface).storeConsentTypeState(NEURA, state, BuildConfig.VERSION_CODE, postConsentTypeCallback);
    }
}
