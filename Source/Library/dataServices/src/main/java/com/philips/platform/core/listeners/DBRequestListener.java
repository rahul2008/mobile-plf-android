package com.philips.platform.core.listeners;

import java.util.List;

public interface DBRequestListener<T> {

    void onSuccess(List<? extends T> data);

    void onFailure(Exception exception);
}
