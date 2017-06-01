package com.philips.platform.core.events;

import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;

public class GetSubjectProfileResponseEvent extends Event {
    UCoreSubjectProfile uCoreSubjectProfile;

    public GetSubjectProfileResponseEvent(UCoreSubjectProfile uCoreSubjectProfile) {
        this.uCoreSubjectProfile = uCoreSubjectProfile;
    }

    public UCoreSubjectProfile getUCoreSubjectProfile() {
        return uCoreSubjectProfile;
    }
}
