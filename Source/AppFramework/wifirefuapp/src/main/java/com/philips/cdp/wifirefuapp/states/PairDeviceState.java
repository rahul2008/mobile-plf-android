package com.philips.cdp.wifirefuapp.states;

import android.content.Context;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philips on 6/8/17.
 */

public class PairDeviceState extends BaseState implements DevicePairingListener {

    private List<UCoreSubjectProfile> subjectProfileList;
    private PairDevicePojo pairDevicePojo;
    private Context context;

    public PairDeviceState(PairDevicePojo pairDevicePojo, List<UCoreSubjectProfile> subjectProfileList, Context context){
        super(context);
        this.context = context;
        this.subjectProfileList = subjectProfileList;
        this.pairDevicePojo = pairDevicePojo;
    }

    @Override
    public void onResponse(boolean b) {

    }

    @Override
    public void onError(DataServicesError dataServicesError) {

    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {

    }

    @Override
    public void start(StateContext stateContext) {
        pairDevice();
    }


    protected void pairDevice(){

        DataServicesManager.getInstance().pairDevices(pairDevicePojo.getDeviceID(),pairDevicePojo.getDeviceType(), getSubjectProfileIdList(subjectProfileList),getSubjectProfileIdList(subjectProfileList),this);
    }

    private List<String> getSubjectProfileIdList(List<UCoreSubjectProfile> subjectProfileList) {
        List<String> subjectProfileIDList = new ArrayList<>();
        for (UCoreSubjectProfile subjectProfile: subjectProfileList) {
            subjectProfileIDList.add(subjectProfile.getGuid());
        }
        return subjectProfileIDList;
    }

}
