package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class CheckDevicePairedStatusState extends BaseState implements DevicePairingListener {

    private PairDevicePojo pairDevicePojo;
    private StateContext stateContext;
    private FragmentLauncher context;
    private ProgressDialog mProgressDialog;

    public CheckDevicePairedStatusState(PairDevicePojo pairDevicePojo,FragmentLauncher context){
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    private void getPairedDevices(){
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
                if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }

            }
        });
    }

    private void dismissProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog!=null && mProgressDialog.isShowing()) {
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
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
        dismissProgressDialog();
        stateContext = new StateContext();

        if(isDevicePaired(list)){
            final List<String> listOfDevices = list;
            context.getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context.getFragmentActivity(),"Device paired already"+listOfDevices.size(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            stateContext.setState(new IsSubjectProfilePresentState(pairDevicePojo,context));
            stateContext.start();
        }


    }

    private boolean isDevicePaired(List<String> list) {
        return list.contains(pairDevicePojo.getDeviceID());
    }

    @Override
    public void start(StateContext stateContext) {
        getPairedDevices();
    }
}
