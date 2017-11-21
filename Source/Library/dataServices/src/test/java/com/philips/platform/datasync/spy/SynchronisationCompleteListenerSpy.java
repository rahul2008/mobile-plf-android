package com.philips.platform.datasync.spy;

import com.philips.platform.core.listeners.SynchronisationCompleteListener;

public class SynchronisationCompleteListenerSpy implements SynchronisationCompleteListener {

    public Exception exception;

    @Override
    public void onSyncComplete() {

    }

    @Override
    public void onSyncFailed(Exception exception) {
        this.exception = exception;
    }
}
