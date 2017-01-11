package com.philips.platform.core.listeners;

import java.util.ArrayList;

public interface DBRequestListener {

    public void onSuccess(ArrayList<? extends Object> data);
    public void onSuccess(Object data);
    public void onFailure(Exception exception);
}
