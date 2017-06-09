package com.philips.cdp.wifirefuapp.ui;

import com.philips.cdp.wifirefuapp.pojo.SubjectProfilePojo;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

/**
 * Created by philips on 6/9/17.
 */

public class CreateSubjectProfileFragmentPresenter implements SubjectProfileListener{

    public CreateSubjectProfileFragmentPresenter(){

    }

    public void createProfile(SubjectProfilePojo subjectProfilePojo){

        if(null != subjectProfilePojo){
            DataServicesManager.getInstance().createSubjectProfile(subjectProfilePojo.getFirstName().toString(),subjectProfilePojo.getDob().toString(),subjectProfilePojo.getGender().toString(),subjectProfilePojo.getWeight(),subjectProfilePojo.getCreationDate().toString(),this);
        }
    }


    @Override
    public void onResponse(boolean b) {

    }

    @Override
    public void onError(DataServicesError dataServicesError) {

    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {

    }
}
