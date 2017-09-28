/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.states;

import android.app.Activity;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.devicepair.ui.IDevicePairingListener;
import com.philips.platform.devicepair.utils.Utility;

import java.util.List;

class CheckConsentState extends AbstractBaseState implements DBRequestListener<ConsentDetail>, DBFetchRequestListner<ConsentDetail> {

    private Activity mActivity;
    private IDevicePairingListener mDeviceStatusListener;

    CheckConsentState(IDevicePairingListener deviceStatusListener, Activity activity) {
        super();
        this.mActivity = activity;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(mActivity)) {
            fetchConsent();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void fetchConsent() {
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> list) {
        boolean accepted = false;
        for (ConsentDetail ormConsentDetail : list) {
            if (ormConsentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
                accepted = true;
            } else {
                accepted = false;
                break;
            }
        }

        if (!accepted) {
            mDeviceStatusListener.onConsentNotAccepted();
        } else {
            mDeviceStatusListener.onProfileNotCreated();
        }
    }

    @Override
    public void onFetchFailure(final Exception e) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void onSuccess(List<? extends ConsentDetail> list) {
    }

    @Override
    public void onFailure(final Exception e) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(e.getMessage());
            }
        });
    }
}
