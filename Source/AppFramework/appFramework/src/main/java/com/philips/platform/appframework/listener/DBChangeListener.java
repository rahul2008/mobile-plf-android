package com.philips.platform.appframework.listener;

import java.util.ArrayList;

import retrofit.RetrofitError;

public interface DBChangeListener {

    public void onSuccess(ArrayList<? extends Object> data);
    public void onSuccess(Object data);
    public void onFailure(Exception exception);
}
