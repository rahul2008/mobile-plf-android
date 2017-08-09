/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

public class THSScheduledVisitsPresenter implements THSBasePresenter,THSGetAppointmentsCallback<List,THSSDKError> {
    THSScheduledVisitsFragment mThsScheduledVisitsFragment;

    public THSScheduledVisitsPresenter(THSScheduledVisitsFragment thsScheduledVisitsFragment) {
        mThsScheduledVisitsFragment = thsScheduledVisitsFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    public void getAppointmentsSince(SDKLocalDate dateSince) throws AWSDKInstantiationException {
        THSManager.getInstance().getAppointments(mThsScheduledVisitsFragment.getContext(),dateSince,this);
    }

    @Override
    public void onResponse(List<Appointment> appointments, SDKError sdkError) {
        AmwellLog.i(AmwellLog.LOG,"appoint response");
        mThsScheduledVisitsFragment.updateList(appointments);
    }

    @Override
    public void onFailure(Throwable throwable) {
        AmwellLog.i(AmwellLog.LOG,"appoint throwable");
    }
}
