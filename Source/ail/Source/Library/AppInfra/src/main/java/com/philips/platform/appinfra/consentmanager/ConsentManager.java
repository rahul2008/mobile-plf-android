/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appinfra.consentmanager;

import android.os.Handler;
import android.os.Looper;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class ConsentManager implements ConsentManagerInterface {

    private final AppInfra mAppInfra;
    private Map<String, ConsentHandlerInterface> consentHandlerMapping = new HashMap<>();

    public ConsentManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
    }

    @Override
    public synchronized void registerHandler(List<String> consentTypes, ConsentHandlerInterface consentHandlerInterface) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                throw new RuntimeException("Consent type already exist");
            consentHandlerMapping.put(consentType, consentHandlerInterface);
        }
    }

    //TODO throw exception in case of key does not exist ?
    @Override
    public synchronized void deregisterHandler(List<String> consentTypes) {
        for (String consentType : consentTypes) {
            if (consentHandlerMapping.containsKey(consentType))
                consentHandlerMapping.remove(consentType);
        }
    }

    protected ConsentHandlerInterface getHandler(String consentType) {
        ConsentHandlerInterface handler = consentHandlerMapping.get(consentType);
        if (handler != null)
            return handler;

        throw new RuntimeException("Handler is not registered for the type " + consentType);
    }

    private void executeHandlerToFetchConsentState(final ConsentDefinition consentDefinition, final FetchConsentCallback callback) throws RuntimeException {
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
    public void fetchConsentState(final ConsentDefinition consentDefinition, final FetchConsentCallback callback) throws RuntimeException {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                executeHandlerToFetchConsentState(consentDefinition, callback);
            }
        });
    }

    @Override
    public void fetchConsentStates(final List<ConsentDefinition> consentDefinitions, final FetchConsentsCallback callback) throws RuntimeException {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final CountDownLatch countDownLatch = new CountDownLatch(consentDefinitions.size());
                List<ConsentManagerCallbackListener> consentManagerCallbackListeners = new ArrayList<>();

                for (ConsentDefinition consentDefinition : consentDefinitions) {
                    ConsentManagerCallbackListener listener = new ConsentManagerCallbackListener(countDownLatch);
                    consentManagerCallbackListeners.add(listener);
                    executeHandlerToFetchConsentState(consentDefinition, listener);
                }

                waitTillThreadsGetsCompleted(countDownLatch);
                postResultOnFetchConsents(consentManagerCallbackListeners, callback);
            }
        });
    }

    @Override
    public void storeConsentState(final ConsentDefinition consentDefinition, final boolean status, final PostConsentCallback callback) throws RuntimeException {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
                List<ConsentTypeCallbackListener> consentTypeCallbackListeners = new ArrayList<>();

                for (String consentType : consentDefinition.getTypes()) {
                    ConsentTypeCallbackListener listener = new ConsentTypeCallbackListener(countDownLatch);
                    consentTypeCallbackListeners.add(listener);
                    getHandler(consentType).storeConsentTypeState(consentType, status, consentDefinition.getVersion(), listener);
                }

                waitTillThreadsGetsCompleted(countDownLatch);
                postResultOnStoreConsent(consentTypeCallbackListeners, callback);
            }
        });
    }

    private void waitTillThreadsGetsCompleted(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "", "");
        }
    }

    private void postResultOnFetchConsent(final ConsentDefinition consentDefinition,
                                          final List<ConsentTypeCallbackListener> consentTypeCallbackListeners, final FetchConsentCallback callback) {

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                ConsentDefinitionStatus consentDefinitionStatus = null;

                for (ConsentTypeCallbackListener consentCallbackListener : consentTypeCallbackListeners) {

                    ConsentStatus consentStatus = consentCallbackListener.consentStatus;
                    if (consentStatus == null) {
                        consentStatus = new ConsentStatus(ConsentStates.inactive, 0);
                    }

                    if (consentCallbackListener.consentError != null) {
                        callback.onGetConsentsFailed(consentCallbackListener.consentError);
                        return;
                    } else if (consentStatus.getConsentState().equals(ConsentStates.inactive)
                            || consentStatus.getConsentState().equals(ConsentStates.rejected)) {
                        callback.onGetConsentsSuccess(getConsentDefinitionState(consentDefinition, consentStatus));
                        return;
                    }

                    consentDefinitionStatus = getConsentDefinitionState(consentDefinition, consentStatus);
                }
                callback.onGetConsentsSuccess(consentDefinitionStatus);
            }
        });

    }

    private ConsentDefinitionStatus getConsentDefinitionState(ConsentDefinition consentDefinition, ConsentStatus consentStatus) {
        ConsentDefinitionStatus consentDefinitionStatus = new ConsentDefinitionStatus();
        consentDefinitionStatus.setConsentDefinition(consentDefinition);
        consentDefinitionStatus.setConsentState(consentStatus.getConsentState());
        consentDefinitionStatus.setConsentVersionState(getConsentVersionStatus(consentDefinition.getVersion(), consentStatus.getVersion()));
        return consentDefinitionStatus;
    }

    private ConsentVersionStates getConsentVersionStatus(int appVersion, int backendVersion) {
        if (appVersion < backendVersion) {
            return ConsentVersionStates.AppVersionIsLower;
        } else if (appVersion == backendVersion) {
            return ConsentVersionStates.InSync;
        }
        return ConsentVersionStates.AppVersionIsHigher;
    }

    private void postResultOnFetchConsents(final List<ConsentManagerCallbackListener> consentManagerCallbackListeners,final FetchConsentsCallback callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                List<ConsentDefinitionStatus> consentDefinitionStatusList = new ArrayList<>();
                for (ConsentManagerCallbackListener consentManagerCallbackListener : consentManagerCallbackListeners) {
                    if (consentManagerCallbackListener.consentError != null) {
                        callback.onGetConsentsFailed(consentManagerCallbackListener.consentError);
                        return;
                    }
                    consentDefinitionStatusList.add(consentManagerCallbackListener.consentDefinitionStatus);
                }
                callback.onGetConsentsSuccess(consentDefinitionStatusList);
            }
        });

    }

    private void postResultOnStoreConsent(final List<ConsentTypeCallbackListener> consentCallbackListeners,final PostConsentCallback postConsentCallback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (ConsentTypeCallbackListener consentTypeCallbackListener : consentCallbackListeners) {
                    if (consentTypeCallbackListener.consentError != null) {
                        postConsentCallback.onPostConsentFailed(consentTypeCallbackListener.consentError);
                        return;
                    }
                }
                postConsentCallback.onPostConsentSuccess();
            }
        });

    }

    private class ConsentManagerCallbackListener implements FetchConsentCallback {
        CountDownLatch countDownLatch;
        ConsentDefinitionStatus consentDefinitionStatus;
        ConsentError consentError;

        ConsentManagerCallbackListener(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onGetConsentsSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
            this.consentDefinitionStatus = consentDefinitionStatus;
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
        ConsentError consentError;
        ConsentStatus consentStatus;
        boolean calledBack;

        ConsentTypeCallbackListener(CountDownLatch countDownLatch) {
            this.calledBack = false;
            this.countDownLatch = countDownLatch;
        }

        private void markCalledBack() {
            countDownLatch.countDown();
            calledBack = true;
        }

        @Override
        public synchronized void onPostConsentFailed(ConsentError error) {
            if (!calledBack) {
                consentError = error;
                markCalledBack();
            }
        }

        @Override
        public synchronized void onPostConsentSuccess() {
            if (!calledBack) {
                markCalledBack();
            }
        }

        @Override
        public synchronized void onGetConsentsSuccess(ConsentStatus consentStatus) {
            if (!calledBack) {
                this.consentStatus = consentStatus;
                markCalledBack();
            }
        }

        @Override
        public synchronized void onGetConsentsFailed(ConsentError error) {
            if (!calledBack) {
                consentError = error;
                markCalledBack();
            }
        }
    }
}
