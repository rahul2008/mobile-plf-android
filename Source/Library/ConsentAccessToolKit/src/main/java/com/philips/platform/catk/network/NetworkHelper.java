/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.RefreshTokenListener;

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

    public void sendRequest(NetworkAbstractModel model) {
        getNetworkController().sendConsentRequest(model);
    }

    public void refreshAccessToken(final RefreshTokenListener refreshTokenListener) {

        ConsentAccessToolKit.getInstance().getCatkComponent().getUser().refreshLoginSession(new RefreshLoginSessionHandler() {
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
                // Need to handle
            }
        });
    }

    static void setInstance(NetworkHelper networkHelper) {
        NetworkHelper.networkHelper = networkHelper;
    }

    void setNetworkController(NetworkController networkController) {
        controller = networkController;
    }
}
