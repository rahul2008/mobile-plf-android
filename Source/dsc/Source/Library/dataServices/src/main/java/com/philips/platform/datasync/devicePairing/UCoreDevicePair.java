package com.philips.platform.datasync.devicePairing;

import java.util.List;

public class UCoreDevicePair {
    private String deviceId;
    private String deviceType;
    private List<String> standardObservationNames;
    private List<String> subjectIds;
    private String relationshipType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public List<String> getStandardObservationNames() {
        return standardObservationNames;
    }

    public void setStandardObservationNames(List<String> standardObservationNames) {
        this.standardObservationNames = standardObservationNames;
    }

    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }
}
