package com.philips.platform.referenceapp.states;

import android.support.v4.app.FragmentTransaction;

import com.philips.platform.referenceapp.consents.ConsentDialogFragment;
import com.philips.platform.referenceapp.pojo.PairDevice;
import com.philips.platform.referenceapp.ui.DeviceStatusListener;
import com.philips.platform.referenceapp.utils.Utility;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class CheckConsentState extends BaseState implements DBRequestListener<ConsentDetail>, DBFetchRequestListner<ConsentDetail>, DBChangeListener {

    private FragmentLauncher mContext;
    private PairDevice mPairDevice;
    DeviceStatusListener mDeviceStatusListener;

    public CheckConsentState(PairDevice pairDevice, DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        this.mContext = context;
        this.mPairDevice = pairDevice;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(mContext.getFragmentActivity())) {
            fetchConsent();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    public void fetchConsent() {
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
            if (ormConsentDetail.getStatus().toString().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
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
            stateContext.setState(new CreateSubjectProfileState(mPairDevice, mDeviceStatusListener, mContext));
            stateContext.start();
        }
    }

    @Override
    public void onFetchFailure(final Exception e) {
        dismissProgressDialog();

        context.getFragmentActivity().runOnUiThread(new Runnable() {
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

        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(e.getMessage());
            }
        });
    }

    public void launchConsentDialog() {
        ConsentDialogFragment consentDialogFragment = new ConsentDialogFragment();
        consentDialogFragment.setFragmentLauncher(mContext);
        consentDialogFragment.setDeviceDetails(mPairDevice);
        consentDialogFragment.setDeviceStatusListener(mDeviceStatusListener);

        FragmentTransaction fragmentTransaction = mContext.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mContext.getParentContainerResourceID(), consentDialogFragment, "ConsentDialogFragment");
        fragmentTransaction.addToBackStack("ConsentDialogFragment");
        fragmentTransaction.commit();
    }
}
