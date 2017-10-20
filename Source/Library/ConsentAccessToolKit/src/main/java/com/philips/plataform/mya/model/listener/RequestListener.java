package com.philips.plataform.mya.model.listener;

import android.os.Message;

/**
 * Created by philips on 10/18/17.
 */

public interface RequestListener {
    void onResponseSuccess(Message msg);
    void onResponseError(Message msg);
}
