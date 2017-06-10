package com.philips.cdp.wifirefuapp.ui;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.pojo.SubjectProfilePojo;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public interface SubjectProfileViewListener {
    PairDevicePojo getDevicePojo();
    FragmentLauncher getFragmentLauncher();
    boolean validateViews();
    SubjectProfilePojo getSubjectProfilePojo();
    void showToastMessage();
}
