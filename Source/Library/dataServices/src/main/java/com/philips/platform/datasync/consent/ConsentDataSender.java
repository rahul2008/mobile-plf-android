package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;


import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentDataSender extends EventMonitor implements DataSender<Consent> {

    @NonNull
    private final Eventing eventing;

    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    public ConsentDataSender(@NonNull final Eventing eventing) {
        this.eventing = eventing;
    }

    @Override
    public boolean sendDataToBackend(@NonNull final List<? extends Consent> dataToSend) {
          if (!dataToSend.isEmpty() && synchronizationState.get() != State.BUSY.getCode()) {
            eventing.post(new ConsentBackendListSaveRequest(dataToSend));
        }

        return false;
    }

    public void onEventAsync(@SuppressWarnings("UnusedParameters") ConsentBackendListSaveResponse responseEvent) {
        synchronizationState.set(State.IDLE.getCode());
    }

    @Override
    public Class<Consent> getClassForSyncData() {
        return Consent.class;
    }

    public boolean isUserInvalid() {
        return false;
    }
}