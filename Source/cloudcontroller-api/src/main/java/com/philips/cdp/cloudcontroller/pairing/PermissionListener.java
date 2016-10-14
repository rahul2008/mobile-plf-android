/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

/**
 * The interface PermissionListener.
 * <p>
 * Provides callback methods to receive notifications about permission events.
 */
public interface PermissionListener {
    /**
     * On permission returned.
     *
     * @param permissionExists the permission exists
     */
    void onPermissionReturned(boolean permissionExists);

    /**
     * On permission removed.
     */
    void onPermissionRemoved();

    /**
     * On permission added.
     */
    void onPermissionAdded();

    /**
     * On call failed.
     */
    void onCallFailed();
}
