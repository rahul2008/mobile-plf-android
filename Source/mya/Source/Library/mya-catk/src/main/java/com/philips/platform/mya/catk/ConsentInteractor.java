/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import android.support.annotation.NonNull;

import com.philips.platform.mya.catk.exception.UnknownConsentType;
import com.philips.platform.mya.catk.listener.CreateConsentResponseListener;
import com.philips.platform.mya.catk.listener.GetConsentForTypeResponseListener;
import com.philips.platform.mya.catk.listener.GetConsentsResponseListener;
import com.philips.platform.mya.catk.mapper.LocaleMapper;
import com.philips.platform.mya.chi.ConsentCallback;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

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

}
