package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SubjectProfileListener;

public class GetSubjectProfileRequestEvent extends Event{

    private String subjectID;
    private SubjectProfileListener subjectProfileListener;

    public GetSubjectProfileRequestEvent(String subjectID, SubjectProfileListener subjectProfileListener){
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
