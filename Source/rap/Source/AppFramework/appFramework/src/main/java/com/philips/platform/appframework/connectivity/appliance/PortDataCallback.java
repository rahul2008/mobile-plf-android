/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.request.Error;

public interface PortDataCallback<T> {
    void onDataReceived(T portProperties);

    void onError(Error error);
}
