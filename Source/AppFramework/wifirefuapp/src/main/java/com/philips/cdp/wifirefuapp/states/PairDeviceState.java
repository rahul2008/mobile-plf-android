package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;

import com.philips.cdp.wifirefuapp.consents.ConsentDetailType;
import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.List;

public class PairDeviceState extends BaseState implements DevicePairingListener {

    private List<UCoreSubjectProfile> subjectProfileList;
    private PairDevice pairDevice;
    private FragmentLauncher context;
    private ProgressDialog mProgressDialog;
    private DeviceStatusListener mDeviceStatusListener;

    public PairDeviceState(PairDevice pairDevice, List<UCoreSubjectProfile> subjectProfileList,
                           DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        this.context = context;
        this.subjectProfileList = subjectProfileList;
        this.pairDevice = pairDevice;
        mDeviceStatusListener = deviceStatusListener;
    }

    private void showProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(context.getFragmentActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Pairing device");
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

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onDevicePaired(pairDevice.getDeviceID());
            }
        });
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        dismissProgressDialog();
        mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
        dismissProgressDialog();
    }

    @Override
    public void start(StateContext stateContext) {
        pairDevice();
    }


    protected void pairDevice() {
        showProgressDialog();
        DataServicesManager.getInstance().pairDevices(pairDevice.getDeviceID(), pairDevice.getDeviceType(),
                getSubjectProfileIdList(subjectProfileList), getStandardObservationNameList(), this);
    }

    private List<String> getSubjectProfileIdList(List<UCoreSubjectProfile> subjectProfileList) {
        List<String> subjectProfileIDList = new ArrayList<>();
        for (UCoreSubjectProfile subjectProfile : subjectProfileList) {
            subjectProfileIDList.add(subjectProfile.getGuid());
        }
        return subjectProfileIDList;
    }

    private List<String> getStandardObservationNameList() {
        List<String> standardObservationNameList = new ArrayList<>();
        standardObservationNameList.add(ConsentDetailType.SLEEP);
        standardObservationNameList.add(ConsentDetailType.WEIGHT);
        standardObservationNameList.add(ConsentDetailType.TEMPERATURE);
        return standardObservationNameList;
    }

}
