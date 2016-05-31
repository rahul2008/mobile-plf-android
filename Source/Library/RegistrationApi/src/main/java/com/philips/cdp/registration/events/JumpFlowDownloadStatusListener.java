/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

/**
 * Created by 310202337 on 12/4/2015.
 */
public interface JumpFlowDownloadStatusListener {

    public void onFlowDownloadSuccess();
    public void onFlowDownloadFailure();
}
