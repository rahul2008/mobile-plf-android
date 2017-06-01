package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SubjectProfileListener;

public class DeleteSubjectProfileRequestEvent extends Event {
    private String subjectID;
    private SubjectProfileListener subjectProfileListener;

    public DeleteSubjectProfileRequestEvent(String subjectID, SubjectProfileListener subjectProfileListener) {
        this.subjectID = subjectID;
        this.subjectProfileListener = subjectProfileListener;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public SubjectProfileListener getSubjectProfileListener() {
        return subjectProfileListener;
    }
}
