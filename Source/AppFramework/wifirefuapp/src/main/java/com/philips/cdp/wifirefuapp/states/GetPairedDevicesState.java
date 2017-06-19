package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class GetPairedDevicesState extends BaseState implements DevicePairingListener {

    private PairDevice pairDevice;
    private StateContext stateContext;
    private FragmentLauncher context;
    private ProgressDialog mProgressDialog;
    private DeviceStatusListener mDeviceStatusListener;

    public GetPairedDevicesState(PairDevice pairDevice, FragmentLauncher context, DeviceStatusListener deviceStatusListener) {
        super(context);
        this.context = context;
        this.pairDevice = pairDevice;
        mDeviceStatusListener = deviceStatusListener;
    }

    private void getPairedDevices() {
        showProgressDialog();
        DataServicesManager.getInstance().getPairedDevices(this);
    }

    private void showProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(context.getFragmentActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Checking device paired status");
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
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        dismissProgressDialog();
        mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
    }

    @Override
    public void onGetPairedDevicesResponse(final List<String> list) {
        dismissProgressDialog();
        stateContext = new StateContext();

        if (isDevicePaired(list)) {
            context.getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceStatusListener.onGetPairedDevices(list);
                    Toast.makeText(context.getFragmentActivity(), "Device paired already", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            stateContext.setState(new GetSubjectProfileState(pairDevice, mDeviceStatusListener, context));
            stateContext.start();
        }
    }

    private boolean isDevicePaired(List<String> list) {
        return list.contains(pairDevice.getDeviceID());
    }

    @Override
    public void start(StateContext stateContext) {
        getPairedDevices();
    }
}
