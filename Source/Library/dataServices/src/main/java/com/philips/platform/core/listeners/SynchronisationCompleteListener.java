package com.philips.platform.core.listeners;

/**
 * Created by 310218660 on 2/5/2017.
 */

public interface SynchronisationCompleteListener {
    void onSyncComplete();
    void onSyncFailed(Exception exception);
}
