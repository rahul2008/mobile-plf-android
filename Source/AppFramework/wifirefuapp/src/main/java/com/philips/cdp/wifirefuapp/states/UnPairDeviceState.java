package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class UnPairDeviceState extends BaseState implements DevicePairingListener {
    private String mDeviceID;
    private FragmentLauncher mContext;
    private ProgressDialog mProgressDialog;
    private DeviceStatusListener mDeviceStatusListener;

    public UnPairDeviceState(String deviceID, DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        mDeviceID = deviceID;
        mDeviceStatusListener = deviceStatusListener;
        mContext = context;
    }

    private void unPairDevice() {
        showProgressDialog();
        DataServicesManager.getInstance().unPairDevice(mDeviceID, this);
    }

    @Override
    void start(StateContext stateContext) {
        unPairDevice();
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onDeviceUnPaired(mDeviceID);
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

    private void showProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(context.getFragmentActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("UnPairing device");
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
}
