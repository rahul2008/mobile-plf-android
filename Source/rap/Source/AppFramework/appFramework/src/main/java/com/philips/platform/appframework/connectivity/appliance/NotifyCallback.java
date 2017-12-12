/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivity.appliance;

import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class NotifyCallback<P extends DICommPort<T>, T extends PortProperties> implements DICommPortListener<P> {

    @Nullable PortDataCallback<T> callback;

    public void setCallback(@Nullable PortDataCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onPortUpdate(P p) {
        if (callback != null) {
            callback.onDataReceived(p.getPortProperties());
        }
    }

    @Override
    public void onPortError(P p, Error error, String s) {
        if (callback != null) {
            callback.onError(error);
        }
    }
}

