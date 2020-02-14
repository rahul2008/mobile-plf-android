/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.consentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains the map of {@link ConsentDefinition} and the {@link ConsentStatusChangedListener}
 */
class ConsentStatusChangeMapper {
    private Map<ConsentDefinition, List<ConsentStatusChangedListener>> consentStatusUpateMapping = new HashMap<>();

    void registerConsentStatusUpdate(ConsentDefinition consentDefinition, ConsentStatusChangedListener listener) {
        ensureListForConsentDefinition(consentDefinition);
        List<ConsentStatusChangedListener> listeners = getConsentStatusChangedListeners(consentDefinition);
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    void unRegisterConsentStatusUpdate(ConsentDefinition consentDefinition, ConsentStatusChangedListener consentStatusChangedListener) {
        List<ConsentStatusChangedListener> consentStatusChangedListeners = getConsentStatusChangedListeners(consentDefinition);
        if (consentStatusChangedListeners != null) {
            consentStatusChangedListeners.remove(consentStatusChangedListener);
            removeKeyIfNoListenersPresent(consentDefinition, consentStatusChangedListeners);
        }
    }

    private void removeKeyIfNoListenersPresent(ConsentDefinition consentDefinition, List<ConsentStatusChangedListener> consentStatusChangedListeners) {
        if (consentStatusChangedListeners.isEmpty()) {
            consentStatusUpateMapping.remove(consentDefinition);
        }
    }

    void consentStatusChanged(@NonNull ConsentDefinition consentDefinition, @Nullable ConsentError consentError, boolean requestedStatus) {
        List<ConsentStatusChangedListener> listeners = getConsentStatusChangedListeners(consentDefinition);
        if (listeners != null) {
            for (ConsentStatusChangedListener consentStatusChangedListener : listeners) {
                consentStatusChangedListener.consentStatusChanged(consentDefinition, consentError, requestedStatus);
            }
        }
    }

    private List<ConsentStatusChangedListener> getConsentStatusChangedListeners(@NonNull ConsentDefinition consentDefinition) {
        return consentStatusUpateMapping.get(consentDefinition);
    }

    private void ensureListForConsentDefinition(ConsentDefinition consentDefinition) {
        List<ConsentStatusChangedListener> consentStatusChangedListeners = getConsentStatusChangedListeners(consentDefinition);
        if (consentStatusChangedListeners == null) {
            consentStatusUpateMapping.put(consentDefinition, new ArrayList<>());
        }
    }
}