package com.philips.platform.baseapp.screens.dataservices.listener;

import java.util.ArrayList;

public interface DBChangeListener {

    void onSuccess(ArrayList<? extends Object> data);

    void onSuccess(Object data);

    void onFailure(Exception exception);
}
