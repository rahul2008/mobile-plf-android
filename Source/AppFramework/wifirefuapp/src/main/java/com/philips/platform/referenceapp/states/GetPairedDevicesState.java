package com.philips.platform.referenceapp.states;

import com.philips.platform.referenceapp.pojo.PairDevice;
import com.philips.platform.referenceapp.ui.DeviceStatusListener;
import com.philips.platform.referenceapp.utils.Utility;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class GetPairedDevicesState extends BaseState implements DevicePairingListener {

    private PairDevice pairDevice;
    private FragmentLauncher context;
    private DeviceStatusListener mDeviceStatusListener;
    private boolean mIsPair;

    public GetPairedDevicesState(PairDevice pairDevice, FragmentLauncher context, DeviceStatusListener deviceStatusListener) {
        super(context);
        this.context = context;
        this.pairDevice = pairDevice;
        mDeviceStatusListener = deviceStatusListener;
        mIsPair = true;
    }

    public GetPairedDevicesState(FragmentLauncher context, DeviceStatusListener deviceStatusListener) {
        super(context);
        this.context = context;
        mDeviceStatusListener = deviceStatusListener;
        mIsPair = false;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(context.getFragmentActivity())) {
            getPairedDevices();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void getPairedDevices() {
        showProgressDialog("Fetching paired devices...");
        DataServicesManager.getInstance().getPairedDevices(this);
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();
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
    public void onGetPairedDevicesResponse(final List<String> list) {
        dismissProgressDialog();

        if (mIsPair) {
            if (isDevicePaired(list)) {
                context.getFragmentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceStatusListener.onGetPairedDevices(list);
                    }
                });
            } else {
                StateContext stateContext = new StateContext();
                stateContext.setState(new GetSubjectProfileState(pairDevice, mDeviceStatusListener, context));
                stateContext.start();
            }
        } else {
            context.getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceStatusListener.onGetPairedDevices(list);
                }
            });
        }
    }

    private boolean isDevicePaired(List<String> list) {
        return list.contains(pairDevice.getDeviceID());
    }
}
