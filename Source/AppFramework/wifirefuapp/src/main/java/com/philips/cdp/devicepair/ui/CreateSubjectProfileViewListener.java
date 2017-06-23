package com.philips.cdp.devicepair.ui;

import com.philips.cdp.devicepair.pojo.SubjectProfile;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

public interface CreateSubjectProfileViewListener {
    boolean validateViews();

    SubjectProfile getSubjectProfile();

    void showToastMessage();

    void onCreateSubjectProfile(List<UCoreSubjectProfile> list);

    void onError(String message);
}
