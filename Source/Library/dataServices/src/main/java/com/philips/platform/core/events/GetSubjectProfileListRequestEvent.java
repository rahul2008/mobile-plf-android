package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SubjectProfileListener;

public class GetSubjectProfileListRequestEvent extends Event{

    SubjectProfileListener subjectProfileListener;

    public GetSubjectProfileListRequestEvent(SubjectProfileListener subjectProfileListener) {
        this.subjectProfileListener = subjectProfileListener;
    }

    public SubjectProfileListener getSubjectProfileListener() {
        return subjectProfileListener;
    }
}
