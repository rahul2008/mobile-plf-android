/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.Appointment;

public interface THSGetAppointmentsCallback<List, THSSDKError> {
    void onResponse(java.util.List<Appointment> appointments, SDKError sdkError);
    void onFailure(Throwable throwable);
}
