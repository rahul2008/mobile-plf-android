package com.philips.platform.core.listeners;

import java.util.ArrayList;

public interface DBRequestListener {

    void onSuccess(ArrayList<? extends Object> data);

    void onSuccess(Object data);

    void onFailure(Exception exception);
}
