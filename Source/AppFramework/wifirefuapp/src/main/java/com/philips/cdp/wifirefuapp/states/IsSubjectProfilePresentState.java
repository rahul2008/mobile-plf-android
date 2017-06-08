package com.philips.cdp.wifirefuapp.states;

import android.content.Context;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class IsSubjectProfilePresentState extends BaseState implements SubjectProfileListener {

    private PairDevicePojo pairDevicePojo;
    private Context context;

    public IsSubjectProfilePresentState(PairDevicePojo pairDevicePojo, Context context){
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    StateContext stateContext;

    protected void createSubjectProfile(){
        //DataServicesManager.getInstance().createSubjectProfile();
    }

    protected void getSubjectProfiles(){
        DataServicesManager.getInstance().getSubjectProfiles(this);
    }

    protected void getSubjectProfile(){
        //DataServicesManager.getInstance().getSubjectProfile();
    }

    protected void deleteSubjectProfile(){
        //DataServicesManager.getInstance().deleteSubjectProfile();
    }

    protected void launchSubjectProfileScreen(){
        //context.getFragmentManager().beginTransaction().add().commit();
    }

    @Override
    public void onResponse(boolean b) {

    }

    @Override
    public void onError(DataServicesError dataServicesError) {

    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        stateContext = new StateContext();
        if(list.size() > 0){

            stateContext.setState(new PairDeviceState(pairDevicePojo, list, context));

        }
        else {
            stateContext.setState(new CheckConsentState(context));
        }
        if(null != stateContext) {
            stateContext.start();
        }
    }

    @Override
    public void start(StateContext stateContext) {
        getSubjectProfiles();
    }
}
