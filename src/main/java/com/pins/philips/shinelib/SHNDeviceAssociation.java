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
        public void onAssociationStarted(SHNDeviceAssociationProcedure shnDeviceAssociationProcedure);
        public void onAssociationStopped();
        public void onAssociationSucceeded(SHNDevice shnDevice);
        public void onAssociationFailed(SHNResult shnError); // The iOS version uses NSError
    }

    private SHNDeviceAssociationListener shnDeviceAssociationListener;
    private SHNDeviceAssociationState shnDeviceAssociationState = SHNDeviceAssociationState.SHNDeviceAssociationStateIdle;
    private List<SHNDevice> associatedDevices;

    public void startAssociationForDeviceType(String deviceTypeName) { throw new UnsupportedOperationException(); }
    public void stopAssociation() { throw new UnsupportedOperationException(); }
}
