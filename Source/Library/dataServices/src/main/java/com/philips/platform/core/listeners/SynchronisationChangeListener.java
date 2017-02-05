package com.philips.platform.core.listeners;

public interface SynchronisationChangeListener {
    void dataPullSuccess();
    void dataPushSuccess();
    void dataPullFail(Exception e);
    void dataPushFail(Exception e);
    void dataSyncComplete();
}
