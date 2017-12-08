/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

import com.philips.platform.core.utils.DataServicesError;

/**
 * Interface Callback for notifying RegisterDeviceToken success or failure for Push Notification
 */
public interface RegisterDeviceTokenListener {
     /**
      * Used for Notifying the Propositions in case of RegisterDeviceToken Success.
      * @param status boolean indicating the register status
      */
     void onResponse(boolean status);

     /**
      * Used for Notifying the Propositions in case of RegisterDeviceToken Failure.
      * @param dataServicesError The Error Response Object returned from Server
      */
     void onError(DataServicesError dataServicesError);
}
