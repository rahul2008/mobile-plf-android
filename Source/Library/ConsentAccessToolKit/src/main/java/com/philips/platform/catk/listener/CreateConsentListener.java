package com.philips.platform.catk.listener;

/**
 * Created by philips on 10/24/17.
 */

public interface CreateConsentListener {

    void onSuccess(int code);
    int onFailure(int code);
}
