package com.philips.cdp.wifirefuapp.observablemodules;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class SubjectProfileObservable implements SubjectProfileListener {
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
