/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.request.Error;

public interface DICommPortListener<P extends DICommPort> {

    /**
     * Called when the (locally) known state of a port has changed. This may happen because of two reasons:
     * <ul>
     * <li>
     * As the result of performing an action on the port. (putProperties(), subscribe(), ...)
     * </li>
     * <li>
     * When a subscription event is received for this port.
     * </li>
     * </ul>
     * <p>
     * <p><b>WARNING:</b> You cannot rely on this callback to be the response to an action you initiated on the port.
     * The received callback might also be a subscription event or the result of another action initiated by another part of the code.</p>
     *
     * @param port The port that received an update.
     */
    void onPortUpdate(P port);

    void onPortError(P port, Error error, @Nullable String errorData);

}
