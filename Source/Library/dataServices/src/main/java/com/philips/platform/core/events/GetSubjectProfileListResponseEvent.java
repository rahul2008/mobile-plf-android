package com.philips.platform.core.events;

import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfileList;

public class GetSubjectProfileListResponseEvent extends Event {
    UCoreSubjectProfileList uCoreSubjectProfileList;

    public GetSubjectProfileListResponseEvent(UCoreSubjectProfileList uCoreSubjectProfileList) {
        this.uCoreSubjectProfileList = uCoreSubjectProfileList;
    }

    public UCoreSubjectProfileList getUCoreSubjectProfileList() {
        return uCoreSubjectProfileList;
    }
}
