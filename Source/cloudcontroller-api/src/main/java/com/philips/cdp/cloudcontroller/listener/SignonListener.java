/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

/**
 * The interface Signon listener.
 * <p>
 * Provides notifications on the sign on status.
 */
public interface SignonListener {
    /**
     * Sign on status.
     *
     * @param signon the signon status, where true means signed on
     */
    void signonStatus(boolean signon);
}
