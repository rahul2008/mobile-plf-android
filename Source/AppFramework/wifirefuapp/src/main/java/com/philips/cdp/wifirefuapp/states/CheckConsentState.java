package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.wifirefuapp.consents.ConsentDialogFragment;
import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
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
    private ProgressDialog mProgressDialog;
    private PairDevice mPairDevice;
    StateContext mStateContext;
    DeviceStatusListener mDeviceStatusListener;

    public CheckConsentState(PairDevice pairDevice, DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        this.mContext = context;
        this.mPairDevice = pairDevice;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void start(StateContext stateContext) {
        fetchConsent();
    }

    public void fetchConsent() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    private void showProgressDialog() {
        mContext.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(mContext.getFragmentActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Checking Consent");
                if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }

            }
        });
    }

    private void dismissProgressDialog() {
        mContext.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });
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
        mStateContext = new StateContext();
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
            mStateContext.setState(new CreateSubjectProfileState(mPairDevice, mDeviceStatusListener, mContext));
            mStateContext.start();
        }
    }

    @Override
    public void onFetchFailure(Exception e) {
        dismissProgressDialog();
    }

    @Override
    public void onSuccess(List<? extends ConsentDetail> list) {
        dismissProgressDialog();
    }

    @Override
    public void onFailure(Exception e) {
        dismissProgressDialog();
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
