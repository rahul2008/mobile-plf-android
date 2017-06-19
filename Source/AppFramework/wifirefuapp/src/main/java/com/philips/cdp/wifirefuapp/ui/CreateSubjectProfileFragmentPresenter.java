package com.philips.cdp.wifirefuapp.ui;

import android.app.ProgressDialog;

import com.philips.cdp.wifirefuapp.pojo.SubjectProfile;
import com.philips.cdp.wifirefuapp.states.PairDeviceState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

public class CreateSubjectProfileFragmentPresenter implements SubjectProfileListener {

    private CreateSubjectProfileViewListener subjectProfileViewListener;
    private StateContext stateContext;
    private DeviceStatusListener mDeviceStatusListener;

    public CreateSubjectProfileFragmentPresenter(CreateSubjectProfileViewListener subjectProfileViewListener, DeviceStatusListener deviceStatusListener) {
        this.subjectProfileViewListener = subjectProfileViewListener;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    public void createProfile() {
        if (subjectProfileViewListener.validateViews()) {
            SubjectProfile subjectProfile = subjectProfileViewListener.getSubjectProfile();
            DataServicesManager.getInstance().createSubjectProfile(subjectProfile.getFirstName().toString(),
                    subjectProfile.getDob().toString(),
                    subjectProfile.getGender().toString(),
                    subjectProfile.getWeight(),
                    subjectProfile.getCreationDate().toString(), this);
        } else {
            subjectProfileViewListener.showToastMessage();
        }
    }


    @Override
    public void onResponse(boolean isCreated) {
        if (isCreated) {
            DataServicesManager.getInstance().getSubjectProfiles(this);
        }
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        subjectProfileViewListener.onError(dataServicesError.getErrorMessage());
    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        subjectProfileViewListener.onCreateSubjectProfile(list);
    }
}
