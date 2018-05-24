package com.philips.platform.appinfra.logging.sync;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.appinfra.logging.CloudConsentProvider;
import com.philips.platform.appinfra.logging.LoggingConfiguration;
import com.philips.platform.appinfra.logging.database.AILCloudLogDBManager;
import com.philips.platform.appinfra.rest.NetworkConnectivityChangeListener;
import com.philips.platform.pif.chi.ConsentChangeListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhishek on 5/14/18.
 */

public class CloudLogSyncManager implements Observer<Integer>, ConsentChangeListener, NetworkConnectivityChangeListener {

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
        consentStatus = new CloudConsentProvider(new DeviceStoredConsentHandler(appInfra)).isCloudLoggingConsentProvided();
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
        if (isInternetAvailable && consentStatus) {
            Log.v("SyncTesting", "Cloud log all conditions met. Starting sync.....");
            return true;
        }
        return false;
    }

    @Override
    public void onChanged(@Nullable Integer currentLogCount) {
        Log.v("SyncTesting", "Inside cloud log db change:: count::" + currentLogCount);
        if (checkWhetherToSyncCloudLog()) {
            Log.v("SyncTesting", "Sync enabled");
            if (currentLogCount!=null && currentLogCount >= loggingConfiguration.getBatchLimit()) {
                threadPoolExecutor.execute(new CloudLogSyncRunnable(appInfra, sharedKey, secretKey, productKey));
            }
        } else {
            Log.v("SyncTesting", "Sync disabled");
            threadPoolExecutor.getQueue().clear();
        }
    }

    @Override
    public void onConsentChanged(String consentType, boolean status) {
        if (CloudConsentProvider.CLOUD.equalsIgnoreCase(consentType)) {
            consentStatus = status;
            if (!consentStatus) {
                threadPoolExecutor.getQueue().clear();
            }else{
                forceSync();
            }
        }
    }

    private void forceSync() {
        if (checkWhetherToSyncCloudLog()) {
            Log.v("SyncTesting", "Inside force sync");
            threadPoolExecutor.execute(new CloudLogSyncRunnable(appInfra, sharedKey, secretKey, productKey));
        }
    }

    @Override
    public void onConnectivityStateChange(boolean isConnected) {
        isInternetAvailable = isConnected;
        forceSync();
    }
}
