/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;
import android.app.FragmentTransaction;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.consents.ConsentDialogFragment;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.ui.DeviceStatusListener;
import com.philips.platform.dprdemo.utils.Utility;

import java.util.List;

class CheckConsentState extends AbstractBaseState implements DBRequestListener<ConsentDetail>, DBFetchRequestListner<ConsentDetail>, DBChangeListener {

    private Activity mActivity;
    private PairDevice mPairDevice;
    private DeviceStatusListener mDeviceStatusListener;

    CheckConsentState(PairDevice pairDevice, DeviceStatusListener deviceStatusListener, Activity activity) {
        super(activity);
        this.mActivity = activity;
        this.mPairDevice = pairDevice;
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
        showProgressDialog("Fetching Consent...");
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    @Override
    public void dBChangeSuccess(SyncType syncType) {
        dismissProgressDialog();
    }

    @Override
    public void dBChangeFailed(Exception e) {
        dismissProgressDialog();
    }

    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> list) {
        dismissProgressDialog();

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
            launchConsentDialog();
        } else {
            StateContext stateContext = new StateContext();
            stateContext.setState(new CreateSubjectProfileState(mPairDevice, mDeviceStatusListener, mActivity));
            stateContext.start();
        }
    }

    @Override
    public void onFetchFailure(final Exception e) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void onSuccess(List<? extends ConsentDetail> list) {
        dismissProgressDialog();
    }

    @Override
    public void onFailure(final Exception e) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(e.getMessage());
            }
        });
    }

    private void launchConsentDialog() {
        ConsentDialogFragment consentDialogFragment = new ConsentDialogFragment();
        consentDialogFragment.setDeviceDetails(mPairDevice);
        consentDialogFragment.setDeviceStatusListener(mDeviceStatusListener);

        int containerId = R.id.user_registration_frame_container;
        FragmentTransaction fragmentTransaction = mActivity.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, consentDialogFragment, ConsentDialogFragment.TAG);
        fragmentTransaction.addToBackStack(ConsentDialogFragment.TAG);
        fragmentTransaction.commit();
    }
}
