/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.consentmanager;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsentManager implements ConsentManagerInterface {

    protected long timeout = 60;
    private final AppInfraInterface mAppInfra;
    private Map<String, ConsentHandlerInterface> consentHandlerMapping = new HashMap<>();
    private Map<String, ConsentDefinition> consentDefinitionMapping = new HashMap<>();
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @VisibleForTesting
    ConsentStatusChangeMapper consentStatusChangeMapper = new ConsentStatusChangeMapper();

    public ConsentManager(AppInfraInterface aAppInfra) {
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

    @Override
    public synchronized void registerConsentDefinitions(List<ConsentDefinition> consentDefinitionList) {
        for (ConsentDefinition consentDefinition : consentDefinitionList) {
            for (String type : consentDefinition.getTypes()) {
                consentDefinitionMapping.put(type, consentDefinition);
            }
        }
    }

    @Override
    public void fetchConsentState(final ConsentDefinition consentDefinition, final FetchConsentCallback callback) throws RuntimeException {
        Executors.newSingleThreadExecutor().execute(() -> executeHandlerToFetchConsentState(consentDefinition, callback));
    }

    @Override
    public void fetchConsentStates(final List<ConsentDefinition> consentDefinitions, final FetchConsentsCallback callback) throws RuntimeException {
        Executors.newSingleThreadExecutor().execute(() -> {
            final CountDownLatch countDownLatch = new CountDownLatch(consentDefinitions.size());
            final List<ConsentManagerCallbackListener> consentManagerCallbackListeners = Collections.synchronizedList(new ArrayList<ConsentManagerCallbackListener>());
            ExecutorService cachedThreadPoolExecuter = Executors.newCachedThreadPool();

            for (final ConsentDefinition consentDefinition : consentDefinitions) {
                cachedThreadPoolExecuter.execute(() -> {
                    ConsentManagerCallbackListener listener = new ConsentManagerCallbackListener(countDownLatch);
                    consentManagerCallbackListeners.add(listener);
                    executeHandlerToFetchConsentState(consentDefinition, listener);
                });
            }
            cachedThreadPoolExecuter.shutdown();
            if (waitTillThreadsGetsCompleted(countDownLatch)) {
                postResultOnFetchConsents(consentManagerCallbackListeners, callback);
            } else {
                postExceptionOnMainThread(callback, new ConsentError("Request Timed out", ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT));
            }
        });
    }

    @Override
    public void storeConsentState(final ConsentDefinition consentDefinition, final boolean status, final PostConsentCallback callback) throws RuntimeException {
        singleThreadExecutor.execute(() -> {
            final CountDownLatch countDownLatch = new CountDownLatch(consentDefinition.getTypes().size());
            List<ConsentTypeCallbackListener> consentTypeCallbackListeners = new ArrayList<>();

            for (String consentType : consentDefinition.getTypes()) {
                ConsentTypeCallbackListener listener = new ConsentTypeCallbackListener(countDownLatch);
                consentTypeCallbackListeners.add(listener);
                getHandler(consentType).storeConsentTypeState(consentType, status, consentDefinition.getVersion(), listener);
            }

            if (waitTillThreadsGetsCompleted(countDownLatch)) {
                postResultOnStoreConsent(consentDefinition, consentTypeCallbackListeners, callback, status);
            } else {
                postExceptionOnMainThread(callback, new ConsentError("Request Timed out", ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT));
            }
        });
    }

    @Override
    public void fetchConsentTypeState(final String type, final FetchConsentCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> executeHandlerToFetchConsentTypeState(type, callback));
    }

    @Nullable
    public ConsentDefinition getConsentDefinitionForType(String consentType) {
        ConsentDefinition consentDefinition = consentDefinitionMapping.get(consentType);
        if (consentDefinition != null) {
            return consentDefinition;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConsentStatusChangedListener(ConsentDefinition consentDefinition, ConsentStatusChangedListener listener) {
        consentStatusChangeMapper.registerConsentStatusUpdate(consentDefinition, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConsentStatusChangedListener(ConsentDefinition consentDefinition, ConsentStatusChangedListener consentStatusChangedListener) {
        consentStatusChangeMapper.unRegisterConsentStatusUpdate(consentDefinition, consentStatusChangedListener);
    }

    protected ConsentHandlerInterface getHandler(String consentType) {
        ConsentHandlerInterface handler = consentHandlerMapping.get(consentType);
        if (handler != null) {
            return handler;
        }
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
        if (waitTillThreadsGetsCompleted(countDownLatch)) {
            postResultOnFetchConsent(consentDefinition, consentTypeCallbackListeners, callback);
        } else {
            postExceptionOnMainThread(callback, new ConsentError("Request Timed out", ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT));
        }
    }

    private void executeHandlerToFetchConsentTypeState(final String type, final FetchConsentCallback callback) throws RuntimeException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<ConsentTypeCallbackListener> consentTypeCallbackListeners = new ArrayList<>();

        ConsentTypeCallbackListener listener = new ConsentTypeCallbackListener(countDownLatch);
        consentTypeCallbackListeners.add(listener);
        getHandler(type).fetchConsentTypeState(type, listener);

        if (waitTillThreadsGetsCompleted(countDownLatch)) {
            postResultOnFetchConsent(getConsentDefinitionForType(type), consentTypeCallbackListeners, callback);
        } else {
            postExceptionOnMainThread(callback, new ConsentError("Request Timed out", ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT));
        }
    }

    private boolean waitTillThreadsGetsCompleted(CountDownLatch countDownLatch) {
        try {
            return countDownLatch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "", "");
        }
        return false;
    }

    private void postResultOnFetchConsent(final ConsentDefinition consentDefinition,
                                          final List<ConsentTypeCallbackListener> consentTypeCallbackListeners, final FetchConsentCallback callback) {

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            ConsentStates consentDefinitionState = null;
            ConsentVersionStates consentDefinitionVersionState = null;
            List<Date> timestampList = new ArrayList<>();
            for (ConsentTypeCallbackListener consentCallbackListener : consentTypeCallbackListeners) {

                ConsentStatus consentStatus = consentCallbackListener.consentStatus;
                if (consentStatus == null) {
                    consentStatus = new ConsentStatus(ConsentStates.inactive, 0, null);
                }

                if (consentCallbackListener.consentError != null) {
                    callback.onGetConsentFailed(consentCallbackListener.consentError);
                    return;
                }

                if (consentStatus.getTimestamp() != null)
                    timestampList.add(consentStatus.getTimestamp());
                ConsentStates consentTypeState = toConsentStatus(consentDefinition, consentStatus);
                if (consentDefinitionState == null || consentTypeState.compareTo(consentDefinitionState) > 0) {
                    consentDefinitionState = consentTypeState;
                }
                ConsentVersionStates consentTypeVersionState = toConsentVersionStatus(consentDefinition, consentStatus);
                if (consentDefinitionVersionState == null || consentTypeVersionState.compareTo(consentDefinitionVersionState) > 0) {
                    consentDefinitionVersionState = consentTypeVersionState;
                }
            }
            callback.onGetConsentSuccess(new ConsentDefinitionStatus(consentDefinitionState, consentDefinitionVersionState, consentDefinition, getLatestTimestamp(timestampList)));
        });

    }

    protected Date getLatestTimestamp(List<Date> timestampList) {
        if (timestampList != null && timestampList.size() > 0)
            return Collections.max(timestampList);
        return null;
    }

    private ConsentStates toConsentStatus(ConsentDefinition consentDefinition, ConsentStatus consentStatus) {
        ConsentStates consentState = consentStatus.getConsentState();
        if (consentDefinition.getVersion() > consentStatus.getVersion()) {
            consentState = ConsentStates.inactive;
        }
        return consentState;
    }

    private ConsentVersionStates toConsentVersionStatus(ConsentDefinition consentDefinition, ConsentStatus consentStatus) {
        ConsentVersionStates consentVersionState;
        if (consentDefinition.getVersion() < consentStatus.getVersion()) {
            consentVersionState = ConsentVersionStates.AppVersionIsLower;
        } else if (consentDefinition.getVersion() == consentStatus.getVersion()) {
            consentVersionState = ConsentVersionStates.InSync;
        } else {
            consentVersionState = ConsentVersionStates.AppVersionIsHigher;
        }
        return consentVersionState;
    }

    private void postResultOnFetchConsents(final List<ConsentManagerCallbackListener> consentManagerCallbackListeners, final FetchConsentsCallback callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            List<ConsentDefinitionStatus> consentDefinitionStatusList = new ArrayList<>();
            for (ConsentManagerCallbackListener consentManagerCallbackListener : consentManagerCallbackListeners) {
                if (consentManagerCallbackListener.consentError != null) {
                    callback.onGetConsentsFailed(consentManagerCallbackListener.consentError);
                    return;
                }
                consentDefinitionStatusList.add(consentManagerCallbackListener.consentDefinitionStatus);
            }
            callback.onGetConsentsSuccess(consentDefinitionStatusList);
        });

    }

    private void postResultOnStoreConsent(ConsentDefinition consentDefinition, final List<ConsentTypeCallbackListener> consentCallbackListeners, final PostConsentCallback postConsentCallback, boolean status) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            ConsentError consentError = null;
            for (ConsentTypeCallbackListener consentTypeCallbackListener : consentCallbackListeners) {
                if (consentTypeCallbackListener.consentError != null) {
                    consentError = consentTypeCallbackListener.consentError;
                    break;
                }
            }
            consentStatusChangeMapper.consentStatusChanged(consentDefinition, consentError, status);
            if (consentError != null) {
                postConsentCallback.onPostConsentFailed(consentError);
            } else {
                postConsentCallback.onPostConsentSuccess();
            }
        });
    }

    private void postExceptionOnMainThread(final FetchConsentsCallback callback, final ConsentError consentError) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> callback.onGetConsentsFailed(consentError));
    }

    private void postExceptionOnMainThread(final FetchConsentCallback callback, final ConsentError consentError) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> callback.onGetConsentFailed(consentError));
    }

    private void postExceptionOnMainThread(PostConsentCallback callback, ConsentError consentError) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> callback.onPostConsentFailed(consentError));
    }

    private class ConsentManagerCallbackListener implements FetchConsentCallback {
        CountDownLatch countDownLatch;
        ConsentDefinitionStatus consentDefinitionStatus;
        ConsentError consentError;

        ConsentManagerCallbackListener(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
            this.consentDefinitionStatus = consentDefinitionStatus;
            countDownLatch.countDown();
        }

        @Override
        public void onGetConsentFailed(ConsentError error) {
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
            this.consentStatus = consentStatus;
            markCalledBack();

        }

        @Override
        public synchronized void onGetConsentsFailed(ConsentError error) {
            consentError = error;
            markCalledBack();
        }
    }
}
