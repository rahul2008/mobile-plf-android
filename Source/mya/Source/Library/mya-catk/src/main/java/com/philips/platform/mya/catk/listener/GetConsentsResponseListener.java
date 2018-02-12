/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.catk.listener;

import android.support.annotation.NonNull;

import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConsentsResponseListener implements ConsentResponseListener {

    private CheckConsentsCallback callback;
    private ConsentsClient consentsClient;

    public GetConsentsResponseListener(@NonNull final CheckConsentsCallback callback, ConsentsClient consentsClient) {
        this.callback = callback;
        this.consentsClient = consentsClient;
    }

    @Override
    public void onResponseSuccessConsent(List<BackendConsent> responseData) {
        if (responseData != null && !responseData.isEmpty()) {
            callback.onGetConsentsSuccess(filterConsentsByDefinitions(responseData));
        } else {
            CatkLogger.d(" BackendConsent : ", "no consent for type found on server");
            callback.onGetConsentsSuccess(new ArrayList<Consent>());
        }
    }

    @Override
    public void onResponseFailureConsent(ConsentNetworkError error) {
        CatkLogger.d(" BackendConsent : ", "response failure:" + error);
        this.callback.onGetConsentsFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
    }

    private List<Consent> filterConsentsByDefinitions(List<BackendConsent> receivedBackendConsents) {
        Map<String, BackendConsent> consentsMap = toMap(receivedBackendConsents);
        List<Consent> consents = new ArrayList<>();
        for (ConsentDefinition definition : consentsClient.getConsentDefinitions()) {
            consents.add(new Consent(getConsents(consentsMap, definition), definition));
        }
        return consents;
    }

    private List<BackendConsent> getConsents(Map<String, BackendConsent> consentsMap, ConsentDefinition definition) {
        List<BackendConsent> backendConsents = new ArrayList<>();
        List<String> types = definition.getTypes();
        for (String type : types) {
            backendConsents.add(consentsMap.get(type));
        }
        return backendConsents;
    }

    private Map<String, BackendConsent> toMap(List<BackendConsent> responseData) {
        Map<String, BackendConsent> map = new HashMap<>();
        for (BackendConsent backendConsent : responseData) {
            map.put(backendConsent.getType(), backendConsent);
        }
        return map;
    }

}
