package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventingMock implements Eventing {

    public List<Moment> backendSavedMoments = new ArrayList<>();
    public boolean backendDataRequestFailed;

    @Override
    public void post(@NonNull Event event) {
        if (event instanceof BackendMomentListSaveRequest) {
            this.backendSavedMoments = ((BackendMomentListSaveRequest) event).getMomentList();
        } else if (event instanceof BackendDataRequestFailed) {
            this.backendDataRequestFailed = true;
        }
    }

    @Override
    public void postSticky(@NonNull Event event) {

    }

    @Override
    public void register(@NonNull Object subscriber) {

    }

    @Override
    public void unregister(@NonNull Object subscriber) {

    }

    @Override
    public boolean isRegistered(@NonNull Object subscriber) {
        return false;
    }

    @Override
    public void removeSticky(@NonNull Event event) {

    }
}