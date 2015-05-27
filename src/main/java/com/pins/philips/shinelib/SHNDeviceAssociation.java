package com.pins.philips.shinelib;

import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociation {
    public enum SHNDeviceAssociationState {
        SHNDeviceAssociationStateIdle, SHNDeviceAssociationStateAssociating
    }
    public interface SHNDeviceAssociationListener {
        void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure);
        void onAssociationStopped();
        void onAssociationSucceeded(SHNDevice shnDevice);
        void onAssociationFailed(SHNResult shnError); // The iOS version uses NSError
    }

    private SHNDeviceAssociationListener shnDeviceAssociationListener;

    private SHNDeviceAssociationState shnDeviceAssociationState = SHNDeviceAssociationState.SHNDeviceAssociationStateIdle;
    private List<SHNDevice> associatedDevices;

    public void setShnDeviceAssociationListener(SHNDeviceAssociationListener shnDeviceAssociationListener) {
        this.shnDeviceAssociationListener = shnDeviceAssociationListener;
    }

    public SHNDeviceAssociationState getShnDeviceAssociationState() {
        return shnDeviceAssociationState;
    }

    public void startAssociationForDeviceType(String deviceTypeName) {
        throw new UnsupportedOperationException();
    }

    public void stopAssociation() {
        throw new UnsupportedOperationException();
    }
}
