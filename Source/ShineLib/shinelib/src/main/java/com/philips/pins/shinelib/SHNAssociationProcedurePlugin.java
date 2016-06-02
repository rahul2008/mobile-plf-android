/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A procedure used to associate with a peripheral. AssociationProcedure is started and stopped by {@link SHNDeviceAssociation}. If association is successful then the peripheral
 * is stored by {@link SHNDeviceAssociation}. In case association procedure needs to expose information to the client of the BlueLib, it should always use the user handler obtained by {@link SHNCentral#getUserHandler()}.
 */
public interface SHNAssociationProcedurePlugin extends SHNAssociationProcedure {

    /**
     * {@link SHNDeviceAssociation} calls start on the association procedure and reports the error to the user if the start result is not {@link SHNResult#SHNOk}.
     * Callbacks for the association results are provided via {@code SHNAssociationProcedureListener}.
     *
     * @return result of the association procedure start
     */
    SHNResult start();

    /**
     * {@link SHNDeviceAssociation} calls stop on the association procedure. Callbacks for the association results are provided via {@code SHNAssociationProcedureListener}.
     */
    void stop();

    /**
     * Specifies if {@link SHNDeviceScanner} is used for scanning for the peripheral.
     *
     * @return true if {@link SHNDeviceScanner} needs to be used, false otherwise
     */
    boolean getShouldScan();

    /**
     * Provides an opportunity for {@link SHNDeviceScanner} to inject found peripherals.
     *
     * @param shnDevice          an instance of {@link SHNDevice} corresponding to the found peripheral
     * @param shnDeviceFoundInfo an instance of {@link SHNDeviceFoundInfo} that give additional information like RSSI
     */
    void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo);

    /**
     * Provides an opportunity for {@link SHNDeviceScanner} to indicate scanning timed out.
     */
    void scannerTimeout();

    /**
     * Provides an opportunity to attach a listener for the association procedure.
     *
     * @param SHNAssociationProcedureListener an instance of the listener to receive callbacks
     */
    void setShnAssociationProcedureListener(SHNAssociationProcedureListener SHNAssociationProcedureListener);

    /**
     * Interface that provides updates for the association result.
     */
    interface SHNAssociationProcedureListener {
        /**
         * Provides a callback to SHNDeviceScanner that scan can be stopped.
         */
        void onStopScanRequest();

        /**
         * Provides a callback for successful association.
         *
         * @param shnDevice that association was successful for
         */
        void onAssociationSuccess(SHNDevice shnDevice);

        /**
         * Provides a callback for unsuccessful association.
         *
         * @param shnDevice for which association has failed
         * @param error     error that occurred during the association
         */
        void onAssociationFailed(SHNDevice shnDevice, SHNResult error);
    }
}
