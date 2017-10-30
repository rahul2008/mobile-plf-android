package com.philips.platform.catk.listener;

import android.os.Message;

/**
 * Created by Maqsood on 10/18/17.
 */

public interface RequestListener {
    void onResponseSuccess(Message msg);
    void onResponseError(Message msg);
}
