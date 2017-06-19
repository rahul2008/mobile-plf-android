package com.philips.cdp.wifirefuapp.ui;

import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.pojo.SubjectProfile;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public interface CreateSubjectProfileViewListener {
    boolean validateViews();

    SubjectProfile getSubjectProfile();

    void showToastMessage();

    void onCreateSubjectProfile(List<UCoreSubjectProfile> list);

    void onError(String message);
}
