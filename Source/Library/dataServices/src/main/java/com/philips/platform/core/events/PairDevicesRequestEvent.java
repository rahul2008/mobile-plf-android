package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DevicePairingListener;

import java.util.List;

public class PairDevicesRequestEvent extends Event {
    private String mDeviceId;
    private String mDeviceType;
    private List<String> mStandardObservationNames;
    private List<String> mSubjectIds;
    private String mRelationshipType;
    private DevicePairingListener mDevicePairingListener;

    public PairDevicesRequestEvent(String deviceId, String deviceType, List<String> standardObservationNames,
                                   List<String> subjectIds, String relationshipType, DevicePairingListener devicePairingListener) {
        mDeviceId = deviceId;
        mDeviceType = deviceType;
        mStandardObservationNames = standardObservationNames;
        mSubjectIds = subjectIds;
        mRelationshipType = relationshipType;
        mDevicePairingListener = devicePairingListener;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public String getDeviceType() {
        return mDeviceType;
    }

    public List<String> getStandardObservationNames() {
        return mStandardObservationNames;
    }

    public List<String> getSubjectIds() {
        return mSubjectIds;
    }

    public DevicePairingListener getDevicePairingListener() {
        return mDevicePairingListener;
    }

    public String getRelationshipType() {
        return mRelationshipType;
    }

    public void setRelationshipType(String mRelationshipType) {
        this.mRelationshipType = mRelationshipType;
    }
}
