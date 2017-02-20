package com.philips.platform.core.listeners;

import java.util.List;

public interface DBRequestListener {

    void onSuccess(List<? extends Object> data);

    void onSuccess(Object data);

    void onFailure(Exception exception);
}
