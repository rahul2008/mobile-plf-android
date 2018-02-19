/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.mya.catk.registry;

import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
    public void fetchConsentState(ConsentDefinition consentDefinition, final CheckConsentsCallback callback) {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentCallbackListener> consentCallbackListeners = new ArrayList<>();
        List<Consent> consentList = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentCallbackListener listener = new ConsentCallbackListener(countDownLatch);
            consentCallbackListeners.add(listener);
            getHandler(consentType).fetchConsentState(consentDefinition, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);

        for (ConsentCallbackListener consentCallbackListener : consentCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                callback.onGetConsentsFailed(consentCallbackListener.consentError);
                return;
            } else {
                consentList = consentCallbackListener.consentList;
            }
        }

        callback.onGetConsentsSuccess(consentList);
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        for (ConsentDefinition consentDefinition : consentDefinitions) {
            fetchConsentState(consentDefinition, callback);
            //TODO Clarify multiple callback should be sent to the caller ?
        }
    }

    @Override
    public void storeConsentState(ConsentDefinition consentDefinition, boolean status, PostConsentCallback callback) {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentCallbackListener> consentCallbackListeners = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentCallbackListener listener = new ConsentCallbackListener(countDownLatch);
            consentCallbackListeners.add(listener);
            getHandler(consentType).storeConsentState(consentDefinition, status, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);

        for (ConsentCallbackListener consentCallbackListener : consentCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                callback.onPostConsentFailed(consentCallbackListener.consentDefinition, consentCallbackListener.consentError);
                return;
            }
        }

        callback.onPostConsentSuccess(consentCallbackListeners.get(0).consent);
    }

    private void waitTillThreadsGetsCompleted(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            CatkLogger.d("InterruptedException", e.getMessage());
        }
    }

    private class ConsentCallbackListener implements CheckConsentsCallback, PostConsentCallback {
        CountDownLatch countDownLatch;
        List<Consent> consentList = new ArrayList<>();
        ConsentDefinition consentDefinition;
        Consent consent;
        ConsentError consentError = null;

        ConsentCallbackListener(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onGetConsentsSuccess(List<Consent> consents) {
            consentList = consents;
            countDownLatch.countDown();
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            consentError = error;
            countDownLatch.countDown();
        }

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            consentDefinition = definition;
            consentError = error;
            countDownLatch.countDown();
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            this.consent = consent;
            countDownLatch.countDown();
        }
    }
}
