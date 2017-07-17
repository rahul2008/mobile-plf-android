/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.dprdemo.pojo.SubjectProfile;

import java.util.List;

class CreateSubjectProfileFragmentPresenter implements SubjectProfileListener {

    private ISubjectProfileListener subjectProfileViewListener;

    CreateSubjectProfileFragmentPresenter(ISubjectProfileListener subjectProfileViewListener) {
        this.subjectProfileViewListener = subjectProfileViewListener;
    }

    void createProfile() {
        if (subjectProfileViewListener.validateViews()) {
            SubjectProfile subjectProfile = subjectProfileViewListener.getSubjectProfile();
            DataServicesManager.getInstance().createSubjectProfile(subjectProfile.getFirstName(),
                    subjectProfile.getDob(),
                    subjectProfile.getGender(),
                    subjectProfile.getWeight(),
                    subjectProfile.getCreationDate(), this);
        } else {
            subjectProfileViewListener.onInvalidInput();
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
