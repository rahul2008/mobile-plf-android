/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.devicepair.pojo.SubjectProfile;

import java.util.List;

class CreateSubjectProfileFragmentPresenter implements SubjectProfileListener {

    private ISubjectProfileListener mSubjectProfileListener;

    CreateSubjectProfileFragmentPresenter(ISubjectProfileListener subjectProfileListener) {
        mSubjectProfileListener = subjectProfileListener;
    }

    void createProfile(SubjectProfile subjectProfile) {
        DataServicesManager.getInstance().createSubjectProfile(subjectProfile.getFirstName(),
                subjectProfile.getDob(),
                subjectProfile.getGender(),
                subjectProfile.getWeight(),
                subjectProfile.getCreationDate(), this);
    }

    @Override
    public void onResponse(boolean isCreated) {
        if (isCreated) {
            DataServicesManager.getInstance().getSubjectProfiles(this);
        }
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        mSubjectProfileListener.onError(dataServicesError.getErrorMessage());
    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> subjectProfileList) {
        mSubjectProfileListener.onSuccess(subjectProfileList);
    }
}
