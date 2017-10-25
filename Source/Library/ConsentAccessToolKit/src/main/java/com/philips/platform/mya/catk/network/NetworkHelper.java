package com.philips.platform.mya.catk.network;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.mya.catk.listener.RefreshTokenListener;
import com.philips.platform.mya.catk.listener.RequestListener;

/**
 * Created by Maqsood on 10/18/17.
 */

public class NetworkHelper {

    private static NetworkHelper networkHelper;
    private NetworkController controller;
    private Context mContext;

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
        mContext = context;
        getNetworkController().sendConsentRequest(context,requestCode, model, requestListener);
    }

    public void refreshAccessToken(final RefreshTokenListener refreshTokenListener){

        new User(mContext).refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                refreshTokenListener.onRefreshSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int errCode) {
                refreshTokenListener.onRefreshFailed(errCode);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {

            }
        });
    }
}
