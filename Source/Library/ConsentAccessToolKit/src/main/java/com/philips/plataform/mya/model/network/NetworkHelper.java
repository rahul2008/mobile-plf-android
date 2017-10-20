package com.philips.plataform.mya.model.network;

import android.content.Context;

import com.philips.plataform.mya.model.listener.RequestListener;

/**
 * Created by Maqsood on 10/18/17.
 */

public class NetworkHelper {

    private static NetworkHelper networkHelper;
    private NetworkController controller;

    public static NetworkHelper getInstance() {
        if (networkHelper == null) {
            networkHelper = new NetworkHelper();
        }
        return networkHelper;
    }

    public NetworkController getNetworkController() {
        if (controller == null) {
            controller = new NetworkController();
        }
        return controller;
    }

    public void sendRequest(Context context, int requestCode, NetworkAbstractModel model, final RequestListener
            requestListener) {
        getNetworkController().sendConsentRequest(context,requestCode, model, requestListener);
    }
}
