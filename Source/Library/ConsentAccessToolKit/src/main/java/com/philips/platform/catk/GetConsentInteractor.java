/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.catk.utils.CatkLogger;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConsentInteractor {

    public interface Callback {
        void onConsentRetrieved(@NonNull final Map<String, RequiredConsent> consent);

        void onConsentFailed(int error);
    }

    @NonNull
    private final ConsentAccessToolKit consentAccessToolKit;
    @NonNull
    private final List<ConsentDefinition> definitions;

    public GetConsentInteractor(@NonNull final ConsentAccessToolKit consentAccessToolKit, @NonNull final List<ConsentDefinition> definitions) {
        this.consentAccessToolKit = consentAccessToolKit;
        this.definitions = definitions;
    }

    public void getConsents(@NonNull final Callback callback) {
        consentAccessToolKit.getConsentDetails(new ConsentViewResponseListener(definitions, callback));
    }

    private class ConsentViewResponseListener implements ConsentResponseListener {

        private List<ConsentDefinition> consentDefinitions;
        private Callback callback;

        ConsentViewResponseListener(@NonNull final List<ConsentDefinition> consentDefinitions, @NonNull final Callback callback) {
            this.consentDefinitions = consentDefinitions;
            this.callback = callback;
        }

        @Override
        public void onResponseSuccessConsent(List<Consent> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                callback.onConsentRetrieved(filterConsentsByDefinitions(responseData));
            } else {
                this.callback.onConsentFailed(-1);
                CatkLogger.d(" Consent : ", "no consent for type found on server");
            }
        }

        @Override
        public int onResponseFailureConsent(int consentError) {
            CatkLogger.d(" Consent : ", "response failure:" + consentError);
            this.callback.onConsentFailed(consentError);
            return 0;
        }

        private Map<String, RequiredConsent> filterConsentsByDefinitions(List<Consent> receivedConsents) {
            Map<String, Consent> consentsMap = toMap(receivedConsents);
            Map<String, RequiredConsent> requiredConsents = new HashMap<>();
            for (ConsentDefinition definition : definitions) {
                requiredConsents.put(definition.getType(), new RequiredConsent(consentsMap.get(definition.getType()), definition));
            }
            return requiredConsents;
        }

        private Map<String, Consent> toMap(List<Consent> responseData) {
            Map<String, Consent> map = new HashMap<>();
            for (Consent consent : responseData) {
                map.put(consent.getType(), consent);
            }
            return map;
        }
    }
}
