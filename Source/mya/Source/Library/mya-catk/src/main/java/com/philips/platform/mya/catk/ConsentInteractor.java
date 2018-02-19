/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import android.support.annotation.NonNull;

import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.exception.UnknownConsentType;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mapper.LocaleMapper;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

public class ConsentInteractor implements ConsentHandlerInterface {

    @NonNull
    private final ConsentsClient consentsClient;

    private Map<String, ConsentDefinition> definitionsByType;

    @Inject
    public ConsentInteractor(@NonNull final ConsentsClient consentsClient) {
        this.consentsClient = consentsClient;
        setupConsentDefinitions(consentsClient.getConsentDefinitions());
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, @NonNull final CheckConsentsCallback callback) {
        fetchLatestConsents(callback);
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean switchChecked, PostConsentCallback callback) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        List<BackendConsent> backendConsents = createConsents(definition, consentStatus);
        consentsClient.createConsent(backendConsents, new CreateConsentResponseListener(definition, backendConsents, callback));
    }

    public void getStatusForConsentType(final String consentType, ConsentCallback callback) {
        if (definitionsByType.get(consentType) == null) {
            throw new UnknownConsentType(consentType, definitionsByType.keySet());
        }
        consentsClient.getStatusForConsentType(consentType, 0, new GetConsentForTypeResponseListener(callback, definitionsByType.get(consentType)));
    }

    public void fetchLatestConsents(@NonNull final CheckConsentsCallback callback) {
        consentsClient.getConsentDetails(new GetConsentsResponseListener(callback, consentsClient));
    }

    private List<BackendConsent> createConsents(ConsentDefinition definition, ConsentStatus status) {
        Locale locale = LocaleMapper.toLocale(definition.getLocale());
        List<BackendConsent> backendConsents = new ArrayList<>();
        List<String> types = definition.getTypes();
        for (String type : types) {
            backendConsents.add(new BackendConsent(locale, status, type, definition.getVersion()));
        }
        return backendConsents;
    }

    private void setupConsentDefinitions(List<ConsentDefinition> consentDefinitions) {
        definitionsByType = new HashMap<>();
        for (ConsentDefinition consentDefinition : consentDefinitions) {
            for (String type : consentDefinition.getTypes()) {
                definitionsByType.put(type, consentDefinition);
            }
        }
    }

    static class GetConsentForTypeResponseListener implements ConsentResponseListener {

        private ConsentDefinition definition;

        private ConsentCallback callback;

        public GetConsentForTypeResponseListener(ConsentCallback callback, ConsentDefinition definition) {
            this.callback = callback;
            this.definition = definition;
        }

        @Override
        public void onResponseSuccessConsent(List<BackendConsent> responseData) {
            BackendConsent backendConsent = null;
            if (responseData != null && !responseData.isEmpty()) {
                backendConsent = responseData.get(0);
            }
            callback.onGetConsentRetrieved(new Consent(backendConsent, definition));
        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            callback.onGetConsentFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }

    }

    static class CreateConsentResponseListener implements CreateConsentListener {

        private final ConsentDefinition definition;

        private final List<BackendConsent> backendConsents;
        private final PostConsentCallback callback;

        public CreateConsentResponseListener(ConsentDefinition definition, List<BackendConsent> backendConsents, PostConsentCallback postConsentCallback) {
            this.definition = definition;
            this.backendConsents = backendConsents;
            this.callback = postConsentCallback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create BackendConsent: ", "Success");
            callback.onPostConsentSuccess(new Consent(backendConsents, definition));
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create BackendConsent: ", "Failed : " + error.getCatkErrorCode());
            callback.onPostConsentFailed(definition, new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }

    static class GetConsentsResponseListener implements ConsentResponseListener {

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


}
