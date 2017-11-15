/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;

import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DataSender {

    @Inject
    public SynchronisationManager synchronisationManager;

    public DataSender() {
        DataServicesManager.getInstance().getAppComponent().injectDataSender(this);
    }

    public enum State {
        IDLE(0), BUSY(1);

        final int state;

        State(int state) {
            this.state = state;
        }

        public int getCode() {
            return state;
        }
    }

    /**
     * Fetch data from server
     *
     * @param dataToSend list of data to be sent
     * @return true if conflict error happened
     */
    public abstract boolean sendDataToBackend(@NonNull List dataToSend);

    public abstract Class getClassForSyncData();

    public void onError(RetrofitError error){
        synchronisationManager.dataPushFail(error);
    }
}

