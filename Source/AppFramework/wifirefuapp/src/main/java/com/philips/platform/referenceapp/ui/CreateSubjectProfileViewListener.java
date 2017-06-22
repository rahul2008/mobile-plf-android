package com.philips.platform.referenceapp.ui;

import com.philips.platform.referenceapp.pojo.PairDevice;
import com.philips.platform.referenceapp.pojo.SubjectProfile;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

public interface CreateSubjectProfileViewListener {
    boolean validateViews();

    SubjectProfile getSubjectProfile();

    void showToastMessage();

    void onCreateSubjectProfile(List<UCoreSubjectProfile> list);

    void onError(String message);
}
