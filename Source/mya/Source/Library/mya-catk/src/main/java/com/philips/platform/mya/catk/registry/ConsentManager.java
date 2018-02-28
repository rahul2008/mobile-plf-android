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
            if (entry.getKey().equals(consentType)) {
                return entry.getValue();
            }
        }
        throw new RuntimeException("Handler is not registered for the type " + consentType);
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
    public void fetchConsentState(ConsentDefinition consentDefinition, final CheckConsentsCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentCallbackListener> consentCallbackListeners = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentCallbackListener listener = new ConsentCallbackListener(countDownLatch);
            consentCallbackListeners.add(listener);
            getHandler(consentType).fetchConsentState(consentDefinition, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnFetchConsent(consentCallbackListeners, callback);
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinitions.size());
        List<ConsentCallbackListener> consentCallbackListeners = new ArrayList<>();

        for (ConsentDefinition consentDefinition : consentDefinitions) {
            ConsentCallbackListener listener = new ConsentCallbackListener(countDownLatch);
            consentCallbackListeners.add(listener);
            fetchConsentState(consentDefinition, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnFetchConsents(consentCallbackListeners, callback);
    }

    @Override
    public void storeConsentState(ConsentDefinition consentDefinition, boolean status, PostConsentCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentCallbackListener> consentCallbackListeners = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentCallbackListener listener = new ConsentCallbackListener(countDownLatch);
            consentCallbackListeners.add(listener);
            getHandler(consentType).storeConsentState(consentDefinition, status, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnStoreConsent(consentCallbackListeners, callback);
    }

    private void waitTillThreadsGetsCompleted(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            CatkLogger.d("InterruptedException", e.getMessage());
        }
    }

    private void postResultOnFetchConsent(List<ConsentCallbackListener> consentCallbackListeners, CheckConsentsCallback callback) {
        List<Consent> consentList = new ArrayList<>();
        for (ConsentCallbackListener consentCallbackListener : consentCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                callback.onGetConsentsFailed(consentCallbackListener.consentError);
                return;
            }
            consentList = consentCallbackListener.consentList;
        }
        callback.onGetConsentsSuccess(consentList);
    }

    private void postResultOnFetchConsents(List<ConsentCallbackListener> consentCallbackListeners, CheckConsentsCallback callback) {
        List<Consent> consentList = new ArrayList<>();
        for (ConsentCallbackListener consentCallbackListener : consentCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                callback.onGetConsentsFailed(consentCallbackListener.consentError);
                return;
            }
            consentList.addAll(consentCallbackListener.consentList);
        }
        callback.onGetConsentsSuccess(consentList);
    }

    private void postResultOnStoreConsent(List<ConsentCallbackListener> consentCallbackListeners, PostConsentCallback postConsentCallback) {
        for (ConsentCallbackListener consentCallbackListener : consentCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                postConsentCallback.onPostConsentFailed(consentCallbackListener.consentDefinition, consentCallbackListener.consentError);
                return;
            }
        }
        postConsentCallback.onPostConsentSuccess(consentCallbackListeners.get(0).consent);
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
