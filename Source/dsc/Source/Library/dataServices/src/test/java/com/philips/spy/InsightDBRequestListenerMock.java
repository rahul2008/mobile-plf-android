package com.philips.spy;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class InsightDBRequestListenerMock implements DBRequestListener<Insight> {
    public List<? extends Insight> successData = null;

    @Override
    public void onSuccess(final List<? extends Insight> data) {
        successData = data;
    }

    @Override
    public void onFailure(final Exception exception) {

    }
}
