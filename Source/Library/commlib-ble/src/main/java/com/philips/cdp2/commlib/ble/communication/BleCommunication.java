/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.communication;

/**
 * This type holds all BLE specific behaviour.
 *
 * @publicApi
 */
interface BleCommunication {

    /**
     * Sets the connection mode.
     * <p>
     * If this is enabled, the connection will not be closed after each request, thereby allowing for a faster data transfer.
     * This could lead to a higher battery drain.
     *
     * @param isContinuous true, if connection should be continuous
     */
    void setContinuousConnection(boolean isContinuous);
}
