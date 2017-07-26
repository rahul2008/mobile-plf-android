package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileRequest;

public class CreateSubjectProfileRequestEvent extends Event {
    private UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest;
    private SubjectProfileListener subjectProfileListener;

    public CreateSubjectProfileRequestEvent(UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest, SubjectProfileListener subjectProfileListener) {
        this.uCoreCreateSubjectProfileRequest = uCoreCreateSubjectProfileRequest;
        this.subjectProfileListener = subjectProfileListener;
    }

    public UCoreCreateSubjectProfileRequest getUCoreCreateSubjectProfileRequest() {
        return uCoreCreateSubjectProfileRequest;
    }

    public SubjectProfileListener getSubjectProfileListener() {
        return subjectProfileListener;
    }
}
