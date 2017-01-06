/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

/**
 * Interface to have callbacks to the registered class.
 */
public interface UIStateListener {
    void onStateComplete(BaseState baseState);
}
