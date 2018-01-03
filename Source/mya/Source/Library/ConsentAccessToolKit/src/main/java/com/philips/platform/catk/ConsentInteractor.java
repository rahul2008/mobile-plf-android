/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.LocaleMapper;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;


public class ConsentInteractor {

    @NonNull
    private final ConsentAccessToolKit consentAccessToolKit;

    private Map<String, ConsentDefinition> definitionsByType;

    @Inject
    public ConsentInteractor(@NonNull final ConsentAccessToolKit consentAccessToolKit) {
        this.consentAccessToolKit = consentAccessToolKit;
        setupConsentDefinitions(consentAccessToolKit.getConsentDefinitions());
    }

    public void fetchLatestConsents(@NonNull final ConsentListCallback callback) {
        consentAccessToolKit.getConsentDetails(new GetConsentsResponseListener(callback, consentAccessToolKit));
    }

    public void checkConsents(@NonNull final ConsentListCallback callback) {
        fetchLatestConsents(callback);
    }

    public void getStatusForConsentType(final String consentType, ConsentCallback callback) {
        if (definitionsByType.get(consentType) == null) {
            throw new UnknownwConsentType(consentType, definitionsByType.keySet());
        }
        consentAccessToolKit.getStatusForConsentType(consentType, 0, new GetConsentForTypeResponseListener(callback, definitionsByType.get(consentType)));
    }

    public void createConsentStatus(ConsentDefinition definition, ConsentInteractor.CreateConsentCallback createConsentCallback, boolean switchChecked) {
        ConsentStatus consentStatus = switchChecked ? ConsentStatus.active : ConsentStatus.rejected;
        List<BackendConsent> backendConsents = createConsents(definition, consentStatus);
        consentAccessToolKit.createConsent(backendConsents, new ConsentInteractor.CreateConsentResponseListener(definition, backendConsents, createConsentCallback));
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

    static class CreateConsentResponseListener implements CreateConsentListener {

        private final ConsentDefinition definition;

        private final List<BackendConsent> backendConsents;
        private final CreateConsentCallback callback;

        CreateConsentResponseListener(ConsentDefinition definition, List<BackendConsent> backendConsents, CreateConsentCallback createConsentCallback) {
            this.definition = definition;
            this.backendConsents = backendConsents;
            this.callback = createConsentCallback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create BackendConsent: ", "Success");
            callback.onCreateConsentSuccess(new Consent(backendConsents, definition));
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create BackendConsent: ", "Failed : " + error.getCatkErrorCode());
            callback.onCreateConsentFailed(definition, error);
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
            callback.onGetConsentFailed(error);
        }

    }

    static class GetConsentsResponseListener implements ConsentResponseListener {

        private ConsentListCallback callback;
        private ConsentAccessToolKit consentAccessToolKit;

        GetConsentsResponseListener(@NonNull final ConsentListCallback callback, ConsentAccessToolKit consentAccessToolKit) {
            this.callback = callback;
            this.consentAccessToolKit = consentAccessToolKit;
        }

        @Override
        public void onResponseSuccessConsent(List<BackendConsent> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                callback.onGetConsentRetrieved(filterConsentsByDefinitions(responseData));
            } else {
                CatkLogger.d(" BackendConsent : ", "no consent for type found on server");
                callback.onGetConsentRetrieved(new ArrayList<Consent>());
            }
        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            CatkLogger.d(" BackendConsent : ", "response failure:" + error);
            this.callback.onGetConsentFailed(error);
        }

        private List<Consent> filterConsentsByDefinitions(List<BackendConsent> receivedBackendConsents) {
            Map<String, BackendConsent> consentsMap = toMap(receivedBackendConsents);
            List<Consent> consents = new ArrayList<>();
            for (ConsentDefinition definition : consentAccessToolKit.getConsentDefinitions()) {
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

    public static class UnknownwConsentType extends RuntimeException {

        private UnknownwConsentType(String unknownType, Set<String> knownTypes) {
            super(buildMessage(unknownType, knownTypes));
        }

        private static String buildMessage(String unknowntype, Collection<String> knownTypes) {
            StringBuilder sB = new StringBuilder("unknown consent type: ");
            sB.append(unknowntype);
            sB.append(". Known types are:");
            for (String type : knownTypes) {
                sB.append(type);
                sB.append(",");
            }
            return sB.toString();
        }

    }

    public interface CreateConsentCallback {
        void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error);

        void onCreateConsentSuccess(Consent consent);
    }

    public interface ConsentCallback {
        void onGetConsentRetrieved(@NonNull final Consent consent);

        void onGetConsentFailed(ConsentNetworkError error);
    }

    public interface ConsentListCallback {
        void onGetConsentRetrieved(@NonNull final List<Consent> consents);

        void onGetConsentFailed(ConsentNetworkError error);
    }
}
