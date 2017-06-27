package com.philips.cdp.devicepair.states;

import com.philips.cdp.devicepair.pojo.PairDevice;
import com.philips.cdp.devicepair.ui.DeviceStatusListener;
import com.philips.cdp.devicepair.utils.Utility;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.List;

public class PairDeviceState extends BaseState implements DevicePairingListener {

//    private List<UCoreSubjectProfile> subjectProfileList;
    private PairDevice pairDevice;
    private FragmentLauncher context;
    private DeviceStatusListener mDeviceStatusListener;

    public PairDeviceState(PairDevice pairDevice, List<UCoreSubjectProfile> subjectProfileList,
                           DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        this.context = context;
//        this.subjectProfileList = subjectProfileList;
        this.pairDevice = pairDevice;
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(context.getFragmentActivity())) {
            pairDevice();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void pairDevice() {
        showProgressDialog("Pairing device...");
        List<String> list = new ArrayList();
//        DataServicesManager.getInstance().pairDevices(pairDevice.getDeviceID(), pairDevice.getDeviceType(),
//                getSubjectProfileIdList(subjectProfileList), getStandardObservationNameList(), "urn:cdp|datareceiver_stg", this);

        DataServicesManager.getInstance().pairDevices(pairDevice.getDeviceID(), pairDevice.getDeviceType(),
                list, list, "urn:cdp|datareceiver_stg", this);
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
    public void onError(final DataServicesError dataServicesError) {
        dismissProgressDialog();

        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
            }
        });

    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
    }

    /*private List<String> getSubjectProfileIdList(List<UCoreSubjectProfile> subjectProfileList) {
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
    }*/

}
