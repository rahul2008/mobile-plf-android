/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.listener;

/**
 * The interface Signon listener.
 * <p>
 * Provides notifications on the sign on status.
 *
 * @publicApi
 */
public interface SignonListener {
    /**
     * Sign on status.
     *
     * @param signon the signon status, where true means signed on
     */
    void signonStatus(boolean signon);
}
