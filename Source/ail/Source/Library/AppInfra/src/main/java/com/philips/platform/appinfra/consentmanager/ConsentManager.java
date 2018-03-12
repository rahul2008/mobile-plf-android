/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionState;
import com.philips.platform.pif.chi.datamodel.ConsentState;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ConsentManager implements ConsentManagerInterface {

    private final AppInfra mAppInfra;
    private Map<String, ConsentHandlerInterface> consentHandlerMapping = new HashMap<>();

    public ConsentManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
    }

    @Override
    public synchronized void register(List<String> consentTypes, ConsentHandlerInterface consentHandlerInterface) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                throw new RuntimeException("Consent type already exist");
            consentHandlerMapping.put(consentType, consentHandlerInterface);
        }
    }

    //TODO throw exception in case of key does not exist ?
    @Override
    public synchronized void deregister(List<String> consentTypes) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                consentHandlerMapping.remove(consentType);
        }
    }

    protected ConsentHandlerInterface getHandler(String consentType) {
        for (Map.Entry<String, ConsentHandlerInterface> entry : consentHandlerMapping.entrySet()) {
            if (entry.getKey().equals(consentType)) {
                return entry.getValue();
            }
        }
        throw new RuntimeException("Handler is not registered for the type " + consentType);
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, final FetchConsentCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentTypeCallbackListener> consentTypeCallbackListeners = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentTypeCallbackListener listener = new ConsentTypeCallbackListener(countDownLatch);
            consentTypeCallbackListeners.add(listener);
            getHandler(consentType).fetchConsentTypeState(consentType, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnFetchConsent(consentDefinition, consentTypeCallbackListeners, callback);
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, FetchConsentsCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinitions.size());
        List<ConsentManagerCallbackListener> consentManagerCallbackListeners = new ArrayList<>();

        for (ConsentDefinition consentDefinition : consentDefinitions) {
            ConsentManagerCallbackListener listener = new ConsentManagerCallbackListener(countDownLatch);
            consentManagerCallbackListeners.add(listener);
            fetchConsentState(consentDefinition, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnFetchConsents(consentManagerCallbackListeners, callback);
    }

    @Override
    public void storeConsentState(ConsentDefinition consentDefinition, boolean status, PostConsentCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
        List<ConsentTypeCallbackListener> consentTypeCallbackListeners = new ArrayList<>();

        for (String consentType : consentDefinition.getTypes()) {
            ConsentTypeCallbackListener listener = new ConsentTypeCallbackListener(countDownLatch);
            consentTypeCallbackListeners.add(listener);
            getHandler(consentType).storeConsentTypeState(consentType, status, listener);
        }

        waitTillThreadsGetsCompleted(countDownLatch);
        postResultOnStoreConsent(consentDefinition, consentTypeCallbackListeners, callback);
    }

    private void waitTillThreadsGetsCompleted(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "", "");
        }
    }

    private void postResultOnFetchConsent(ConsentDefinition consentDefinition,
                                          List<ConsentTypeCallbackListener> consentTypeCallbackListeners, FetchConsentCallback callback) {
        ConsentDefinitionState consentDefinitionState = null;
        for (ConsentTypeCallbackListener consentCallbackListener : consentTypeCallbackListeners) {
            if (consentCallbackListener.consentError != null) {
                callback.onGetConsentsFailed(consentCallbackListener.consentError);
                return;
            } else if (consentCallbackListener.consentState.getConsentStatus().equals(ConsentStatus.inactive)
                    || consentCallbackListener.consentState.getConsentStatus().equals(ConsentStatus.rejected)) {
                callback.onGetConsentsSuccess(consentDefinitionState);
            }
            consentDefinitionState = getConsentDefinitionState(consentDefinition, consentCallbackListener.consentState);
        }
        callback.onGetConsentsSuccess(consentDefinitionState);
    }

    private ConsentDefinitionState getConsentDefinitionState(ConsentDefinition consentDefinition, ConsentState consentState) {
        ConsentDefinitionState consentDefinitionState = new ConsentDefinitionState();
        consentDefinitionState.setConsentDefinition(consentDefinition);
        consentDefinitionState.setConsentStatus(consentState.getConsentStatus());
        consentDefinitionState.setConsentVersionStatus(getConsentVersionStatus(consentDefinition.getVersion(), consentState.getVersion()));
        return consentDefinitionState;
    }

    private ConsentVersionStatus getConsentVersionStatus(int appVersion, int backendVersion) {
        if (appVersion < backendVersion) {
            return ConsentVersionStatus.AppVersionIsLower;
        } else if (appVersion == backendVersion) {
            return ConsentVersionStatus.InSync;
        }
        return ConsentVersionStatus.AppVersionIsHigher;
    }

    private void postResultOnFetchConsents(List<ConsentManagerCallbackListener> consentManagerCallbackListeners, FetchConsentsCallback callback) {
        List<ConsentDefinitionState> consentDefinitionStateList = new ArrayList<>();
        for (ConsentManagerCallbackListener consentManagerCallbackListener : consentManagerCallbackListeners) {
            if (consentManagerCallbackListener.consentError != null) {
                callback.onGetConsentsFailed(consentManagerCallbackListener.consentError);
                return;
            }
            consentDefinitionStateList.addAll(Collections.singletonList(consentManagerCallbackListener.consentDefinitionState));
        }
        callback.onGetConsentsSuccess(consentDefinitionStateList);
    }

    private void postResultOnStoreConsent(ConsentDefinition consentDefinition,
                                          List<ConsentTypeCallbackListener> consentCallbackListeners, PostConsentCallback postConsentCallback) {
        ConsentDefinitionState consentDefinitionState = null;
        for (ConsentTypeCallbackListener consentTypeCallbackListener : consentCallbackListeners) {
            if (consentTypeCallbackListener.consentError != null) {
                postConsentCallback.onPostConsentFailed(consentTypeCallbackListener.consentError);
                return;
            }
            consentDefinitionState = getConsentDefinitionState(consentDefinition, consentTypeCallbackListener.consentState);
        }
        postConsentCallback.onPostConsentSuccess(consentDefinitionState);
    }

    private class ConsentManagerCallbackListener implements FetchConsentCallback {
        CountDownLatch countDownLatch;
        ConsentDefinitionState consentDefinitionState;
        ConsentError consentError = null;

        ConsentManagerCallbackListener(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onGetConsentsSuccess(ConsentDefinitionState consentDefinitionState) {
            this.consentDefinitionState = consentDefinitionState;
            countDownLatch.countDown();
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            consentError = error;
            countDownLatch.countDown();
        }

    }

    private class ConsentTypeCallbackListener implements FetchConsentTypeStateCallback, PostConsentTypeCallback {
        CountDownLatch countDownLatch;
        ConsentError consentError = null;
        ConsentState consentState = null;

        ConsentTypeCallbackListener(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onPostConsentFailed(ConsentError error) {
            consentError = error;
            countDownLatch.countDown();
        }

        @Override
        public void onPostConsentSuccess(ConsentState consentState) {
            this.consentState = consentState;
            countDownLatch.countDown();
        }

        @Override
        public void onGetConsentsSuccess(ConsentState consentState) {
            this.consentState = consentState;
            countDownLatch.countDown();
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            consentError = error;
            countDownLatch.countDown();
        }
    }
}
