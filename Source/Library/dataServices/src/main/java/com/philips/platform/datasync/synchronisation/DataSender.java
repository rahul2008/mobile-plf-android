/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DataSender<T> {
    enum State {
        IDLE(0), BUSY(1);

        private final int state;

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
    boolean sendDataToBackend(@NonNull List<? extends T> dataToSend);

    Class<? extends T> getClassForSyncData();
}

