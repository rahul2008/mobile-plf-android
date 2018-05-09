package com.philips.platform.ths.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class THSNetworkStateListener extends BroadcastReceiver{

    public static ConnectionReceiverListener connectionReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectionReceiverListener != null && !isConnected) {
            connectionReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public interface ConnectionReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
