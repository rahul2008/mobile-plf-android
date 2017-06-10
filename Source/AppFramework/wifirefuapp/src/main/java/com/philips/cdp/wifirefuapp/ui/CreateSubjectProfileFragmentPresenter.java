package com.philips.cdp.wifirefuapp.ui;

import com.philips.cdp.wifirefuapp.pojo.SubjectProfilePojo;
import com.philips.cdp.wifirefuapp.states.PairDeviceState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

/**
 * Created by philips on 6/9/17.
 */

public class CreateSubjectProfileFragmentPresenter implements SubjectProfileListener{

    private SubjectProfileViewListener subjectProfileViewListener;
    private StateContext stateContext;

    public CreateSubjectProfileFragmentPresenter(SubjectProfileViewListener subjectProfileViewListener){
        this.subjectProfileViewListener = subjectProfileViewListener;
    }

    public void createProfile(){

        if(subjectProfileViewListener.validateViews()) {
            SubjectProfilePojo subjectProfilePojo = subjectProfileViewListener.getSubjectProfilePojo();

            DataServicesManager.getInstance().createSubjectProfile(subjectProfilePojo.getFirstName().toString(),
                    subjectProfilePojo.getDob().toString(),
                    subjectProfilePojo.getGender().toString(),
                    subjectProfilePojo.getWeight(),
                    subjectProfilePojo.getCreationDate().toString(), this);
        }else {
            subjectProfileViewListener.showToastMessage();
        }
    }


    @Override
    public void onResponse(boolean isCreated) {
        if(isCreated){
            DataServicesManager.getInstance().getSubjectProfiles(this);
        }
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        DataServicesManager.getInstance().getSubjectProfiles(this);
    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        stateContext = new StateContext();
        stateContext.setState(new PairDeviceState(subjectProfileViewListener.getDevicePojo(),list,subjectProfileViewListener.getFragmentLauncher()));
        stateContext.start();
    }
}
