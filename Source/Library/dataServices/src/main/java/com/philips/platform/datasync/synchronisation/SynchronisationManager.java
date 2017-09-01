package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.listeners.SynchronisationChangeListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SynchronisationManager implements SynchronisationChangeListener {

    private volatile boolean isSyncComplete = true;

    public SynchronisationManager(){
        DataServicesManager.getInstance().getAppComponant().injectSynchronisationManager(this);
    }

    @Inject
    Eventing mEventing;

    SynchronisationCompleteListener mSynchronisationCompleteListner;

    public void startSync(SynchronisationCompleteListener synchronisationCompleteListner) {
        synchronized (this) {
            mSynchronisationCompleteListner = synchronisationCompleteListner;
            if (isSyncComplete) {
                isSyncComplete = false;
                mEventing.post(new ReadDataFromBackendRequest(null));
            }
        }
    }

    @Override
    public void dataPullSuccess() {
        mEventing.post(new WriteDataToBackendRequest());
    }

    @Override
    public void dataPushSuccess() {

    }

    @Override
    public void dataPullFail(Exception e) {
        isSyncComplete = true;
        if (mSynchronisationCompleteListner != null)
            mSynchronisationCompleteListner.onSyncFailed(e);
        mSynchronisationCompleteListner = null;
    }

    @Override
    public void dataPushFail(Exception e) {
        isSyncComplete = true;
        if (mSynchronisationCompleteListner != null)
            mSynchronisationCompleteListner.onSyncFailed(e);
        mSynchronisationCompleteListner = null;
    }

    @Override
    public void dataSyncComplete() {
        isSyncComplete = true;
        if (mSynchronisationCompleteListner != null)
            mSynchronisationCompleteListner.onSyncComplete();
        mSynchronisationCompleteListner = null;
    }

    public void stopSync() {
        isSyncComplete = true;
        mSynchronisationCompleteListner = null;
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
//                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
//                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
