/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A procedure used to associate with a peripheral. AssociationProcedure is started and stopped by SHNDeviceAssociation. If association is successful then the peripheral
 * is stored by SHNDeviceAssociation. In case association procedure wants to expose information to the client of the BlueLib. it should always use the user handler of {@link SHNCentral#getUserHandler()}.
 */
public interface SHNAssociationProcedurePlugin extends SHNAssociationProcedure {

    /**
     * SHNDeviceAssociation calls start on the association procedure and reports the error to the user if it was not started with SHNResult.SHNOk.
     * Callbacks for the association results are provided via SHNAssociationProcedureListener
     *
     * @return result of the association procedure start
     */
    SHNResult start();

    /**
     * SHNDeviceAssociation calls stop on the association procedure. Callbacks for the association results are provided via SHNAssociationProcedureListener
     */
    void stop();

    /**
     * Specifies if SHNDeviceScanner is used for scanning for the peripheral.
     *
     * @return true if SHNDeviceScanner  needs to be used, false otherwise
     */
    boolean getShouldScan();

    /**
     * Provides an opportunity for SHNDeviceScanner to inject found peripherals
     *
     * @param shnDevice          an instance of {@link SHNDevice} corresponding to the found peripheral
     * @param shnDeviceFoundInfo an instance of {@link SHNDeviceFoundInfo} that give additional information like RSSI
     */
    void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo);

    /**
     * Provides an opportunity for SHNDeviceScanner to indicate scanning timed out
     */
    void scannerTimeout();

    /**
     * Provides an opportunity to attach a listener for the association procedure.
     */
    void setShnAssociationProcedureListener(SHNAssociationProcedureListener shnAssociationProcedureListener);

    /**
     * Interface that provides updates for the association result
     */
    interface SHNAssociationProcedureListener {
        /**
         * Provides an callback to SHNDeviceScanner that scan can be stopped
         */
        void onStopScanRequest();

        /**
         * Provides an callback for successful association
         *
         * @param shnDevice that association has succeeded for
         */
        void onAssociationSuccess(SHNDevice shnDevice);

        /**
         * Provides an callback for unsuccessful association
         *
         * @param shnDevice that association has failed for
         */
        void onAssociationFailed(SHNDevice shnDevice, SHNResult error);
    }
}
