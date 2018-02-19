/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.mya.catk.registry;

import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentManager implements ConsentRegistryInterface {

    private Map<String, ConsentHandlerInterface> consentHandlerMapping = new HashMap<>();

    @Override
    public synchronized void register(List<String> consentTypes, ConsentHandlerInterface consentHandlerInterface) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                throw new RuntimeException("Consent type already exist");
            consentHandlerMapping.put(consentType, consentHandlerInterface);
        }
    }

    @Override
    public ConsentHandlerInterface getHandler(String consentType) {
        for (Map.Entry<String, ConsentHandlerInterface> entry : consentHandlerMapping.entrySet()) {
            if (entry.getKey().equals(consentType))
                return entry.getValue();
        }
        return null;
    }

    //TODO throw exception in case of key does not exist ?
    @Override
    public synchronized void removeHandler(List<String> consentTypes) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                consentHandlerMapping.remove(consentType);
        }
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
        for (String consentType : consentDefinition.getTypes()) {
            getHandler(consentType).fetchConsentState(consentDefinition, callback); //Should have null check to throw exception in case of no handler registered?
        }
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        for (ConsentDefinition consentDefinition : consentDefinitions) {
            for (String consentType : consentDefinition.getTypes()) {
                getHandler(consentType).fetchConsentState(consentDefinition, callback); //Should have null check or throw exception?
            }
        }
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        for (String consentType : definition.getTypes()) {
            getHandler(consentType).storeConsentState(definition, status, callback); //Should have null check or throw exception?
        }
    }
}
