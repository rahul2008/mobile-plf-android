/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.pif.DataInterface.MEC.listeners;

/**
 * The interface Cart listener.
 */
public interface CartListener {
    /**
     * On failure.
     *
     * @param exception the exception
     * @since 2002.1.0
     */
    void onFailure(Exception exception);
}
