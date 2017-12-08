package com.philips.testing.verticals;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.core.utils.EventingImpl;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by indrajitkumar on 13/12/16.
 */

public class EventingImplMock extends EventingImpl {
    public EventingImplMock(@NonNull EventBus eventBus, @NonNull Handler handler) {
        super(eventBus, handler);
    }
}
