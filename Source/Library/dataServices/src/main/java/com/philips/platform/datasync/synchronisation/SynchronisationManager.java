package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.listeners.SynchronisationChangeListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;

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
    }

    @Override
    public void dataPushFail(Exception e) {
        isSyncComplete = true;
        if (mSynchronisationCompleteListner != null)
            mSynchronisationCompleteListner.onSyncFailed(e);
    }

    @Override
    public void dataSyncComplete() {
        isSyncComplete = true;
        if (mSynchronisationCompleteListner != null)
            mSynchronisationCompleteListner.onSyncComplete();
    }

    public void stopSync() {
        isSyncComplete = true;
    }
}
