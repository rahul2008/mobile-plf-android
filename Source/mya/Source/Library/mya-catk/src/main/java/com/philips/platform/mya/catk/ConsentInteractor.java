/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.philips.platform.mya.catk.exception.UnknownConsentType;
import com.philips.platform.mya.catk.listener.CreateConsentResponseListener;
import com.philips.platform.mya.catk.listener.GetConsentForTypeResponseListener;
import com.philips.platform.mya.catk.listener.GetConsentsResponseListener;
import com.philips.platform.pif.chi.ConsentCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import android.support.annotation.NonNull;

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
        String locale = consentsClient.getAppInfra().getInternationalization().getBCP47UILocale();

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
