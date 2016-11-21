package com.philips.cdp.registration;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by 310190722 on 10/19/2016.
 */
public class HttpClientServiceReceiver extends ResultReceiver {

    private Listener listener;

    public HttpClientServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (listener != null)
            listener.onReceiveResult(resultCode, resultData);
    }


    public static interface Listener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

}
