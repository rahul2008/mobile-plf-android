package com.philips.platform.appinfra.logging.sync;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.rest.NetworkConnectivityChangeListener;
import com.philips.platform.pif.chi.ConsentChangeListener;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhishek on 5/14/18.
 */

public class CloudLogSyncManager implements Observer<Integer>, NetworkConnectivityChangeListener, ConsentChangeListener {

    private static CloudLogSyncManager cloudLogSyncManager;

    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> mSyncDataWorkQueue;

    private ThreadPoolExecutor threadPoolExecutor;

    private AppInfra appInfra;

    private LoggingConfiguration loggingConfiguration;

    private LiveData<Integer> dbLogCount;

    private String secretKey, sharedKey, productKey;

    private boolean consentStatus;

    private boolean isInternetAvailable;

    private CloudLogSyncManager(AppInfra appInfra, final LoggingConfiguration loggingConfiguration) {
        this.appInfra = appInfra;
        this.loggingConfiguration = loggingConfiguration;
        mSyncDataWorkQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mSyncDataWorkQueue);
        dbLogCount = AILCloudLogDBManager.getInstance(appInfra).getLogCount();
        dbLogCount.observeForever(this);
        secretKey = loggingConfiguration.getCLSecretKey();
        sharedKey = loggingConfiguration.getCLSharedKey();
        productKey = loggingConfiguration.getCLProductKey();
        isInternetAvailable = appInfra.getRestClient().isInternetReachable();
        appInfra.getRestClient().registerNetworkChnageListener(this);
        forceSync();

    }

    public static CloudLogSyncManager getInstance(AppInfra appInfra, LoggingConfiguration loggingConfiguration) {
        if (cloudLogSyncManager == null) {
            cloudLogSyncManager = new CloudLogSyncManager(appInfra, loggingConfiguration);
        }
        return cloudLogSyncManager;
    }

    public boolean checkWhetherToSyncCloudLog() {
        //Add consent part here
        if (isInternetAvailable && checkIfSecretKeyAnsSharedKeyAvailable()) {
            return true;
        }
        return false;
    }

    private boolean checkIfSecretKeyAnsSharedKeyAvailable() {
        if (TextUtils.isEmpty(secretKey) || TextUtils.isEmpty(sharedKey) || TextUtils.isEmpty(productKey)) {
            return false;
        }
        return true;
    }

    @Override
    public void onChanged(final @Nullable Integer currentLogCount) {
        Log.v("SyncTesting", "Inside cloud log db change:: count::" + currentLogCount);
        if (checkWhetherToSyncCloudLog()) {
            if (currentLogCount != null && currentLogCount >= loggingConfiguration.getBatchLimit()) {
                checkForConsentAndSync();
            }
        } else {
            Log.v("SyncTesting", "Sync disabled");
            threadPoolExecutor.getQueue().clear();
        }

    }

    private void checkForConsentAndSync() {
        try {
            ConsentDefinition consentDefinition = appInfra.getConsentManager().getConsentDefinitionForType(appInfra.getLogging().getCloudLoggingConsentIdentifier());
            if (consentDefinition != null) {
                appInfra.getConsentManager().fetchConsentState(consentDefinition, new FetchConsentCallback() {
                    @Override
                    public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                        if (ConsentStates.active.equals(consentDefinitionStatus.getConsentState())) {
                            threadPoolExecutor.execute(new CloudLogSyncRunnable(appInfra, sharedKey, secretKey, productKey));
                        } else {
                            Log.v("SyncTesting", "Sync disabled");
                            threadPoolExecutor.getQueue().clear();
                        }
                    }

                    @Override
                    public void onGetConsentFailed(ConsentError error) {
                        Log.v("SyncTesting", "Failed to get consent status");
                    }
                });
            }
        } catch (RuntimeException exception) {
            Log.e("SyncTesting", "Consent definition is not registered yet");
        }
    }

    private void forceSync() {
        Log.v("SyncTesting", "Inside force sync");
        if (checkWhetherToSyncCloudLog()) {
            checkForConsentAndSync();
        }

    }

    @Override
    public void onConsentChanged(String consentType, boolean status) {
        if (appInfra.getLogging().getCloudLoggingConsentIdentifier().equalsIgnoreCase(consentType)) {
            consentStatus = status;
            if (!consentStatus) {
                threadPoolExecutor.getQueue().clear();
            } else {
                forceSync();
            }
        }
    }

    @Override
    public void onConnectivityStateChange(boolean isConnected) {
        isInternetAvailable = isConnected;
        forceSync();
    }
}
