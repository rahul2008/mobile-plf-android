package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;

import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class GetSubjectProfileState extends BaseState implements SubjectProfileListener {

    private PairDevice pairDevice;
    private FragmentLauncher context;
    private ProgressDialog mProgressDialog;
    StateContext stateContext;
    DeviceStatusListener mDeviceStatusListener;

    public GetSubjectProfileState(PairDevice pairDevice, DeviceStatusListener listener, FragmentLauncher context) {
        super(context);
        this.context = context;
        this.pairDevice = pairDevice;
        this.mDeviceStatusListener = listener;
    }

    private void showProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(context.getFragmentActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Checking if subject profile is present");
                if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }

            }
        });
    }

    private void dismissProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });
    }


    protected void getSubjectProfiles() {
        showProgressDialog();
        DataServicesManager.getInstance().getSubjectProfiles(this);
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        dismissProgressDialog();
    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        dismissProgressDialog();
        stateContext = new StateContext();
        if (list.size() > 0) {
            stateContext.setState(new PairDeviceState(pairDevice, list, mDeviceStatusListener, context));
        } else {
            stateContext.setState(new CheckConsentState(pairDevice, mDeviceStatusListener, context));
        }

        if (null != stateContext) {
            stateContext.start();
        }
    }

    @Override
    public void start(StateContext stateContext) {
        getSubjectProfiles();
    }
}
