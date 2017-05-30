package com.philips.platform.core.listeners;

import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

import java.util.List;

public interface SubjectProfileListener {
    void onResponse(boolean status);

    void onError(DataServicesError dataServicesError);

    void onGetSubjectProfiles(List<UCoreSubjectProfile> uCoreSubjectProfileList);
}
