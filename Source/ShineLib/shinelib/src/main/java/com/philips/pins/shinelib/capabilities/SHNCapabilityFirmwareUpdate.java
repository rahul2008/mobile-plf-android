/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;

/**
 * Interface to upload and deploy firmware to a peripheral.
 */
public interface SHNCapabilityFirmwareUpdate extends SHNCapability {

    /**
     * Possible states of the firmware update capability.
     */
    enum SHNFirmwareUpdateState {
        /**
         * Idle state. Firmware upload can only be started from {@code SHNFirmwareUpdateStateIdle} state.
         */
        SHNFirmwareUpdateStateIdle,
        /**
         * Preparing state. In this state, the peripheral is preparing for receiving a firmware image.
         * Note that a devices may not go through this state.
         */
        SHNFirmwareUpdateStatePreparing,
        /**
         * Uploading state. Call {@code abort()} to interrupt the current upload.
         */
        SHNFirmwareUpdateStateUploading,
        /**
         * Verifying state. In this state the peripheral is verifying the received firmware image.
         * Note that a devices may not go through this state.
         */
        SHNFirmwareUpdateStateVerifying,
        /**
         * Deploying state.
         */
        SHNFirmwareUpdateStateDeploying;
    }

    /**
     * Indicates if the peripheral support upload without deploy.
     *
     * @return {@code true} if upload is possible without mandatory deploy, {@code false} otherwise
     */
    boolean supportsUploadWithoutDeploy();

    /**
     * Start firmware upload.
     * <p/>
     * Firmware upload can only be started in {@code SHNFirmwareUpdateStateIdle} state. The callback is provided via registered {@code SHNCapabilityFirmwareUpdateListener}.
     *
     * @param firmwareData
     */
    void uploadFirmware(byte[] firmwareData);

    /**
     * Abort current running firmware upload. The callback is provided via registered {@code SHNCapabilityFirmwareUpdateListener}.
     */
    void abortFirmwareUpload();

    /**
     * Deploy uploaded firmware image. The callback is provided via registered {@code SHNCapabilityFirmwareUpdateListener}.
     */
    void deployFirmware();

    /**
     * Return information about the current uploading firmware.
     *
     * @param shnFirmwareInfoResultListener callback to receive the result
     */
    void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener);

    /**
     * Set callback to receive results of firmware upload and deploy.
     *
     * @param shnCapabilityFirmwareUpdateListener to receive updates
     */
    void setSHNCapabilityFirmwareUpdateListener(SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener);

    /**
     * Return current state of the capability.
     *
     * @return current state of the capability
     */
    SHNFirmwareUpdateState getState();

    /**
     * Interface to receive results of firmware upload and deploy.
     */
    interface SHNCapabilityFirmwareUpdateListener {
        /**
         * Indicates that the state of the capability has changed.
         *
         * @param shnCapabilityFirmwareUpdate that changed state
         */
        void onStateChanged(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);

        /**
         * Indicates the progress of the firmware upload.
         *
         * @param shnCapabilityFirmwareUpdate that made the progress
         * @param progress                    current progress
         */
        void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, float progress);

        /**
         * Indicates that the upload has failed.
         *
         * @param shnCapabilityFirmwareUpdate that failed to upload the firmware image
         * @param shnResult                   reason to fail
         */
        void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);

        /**
         * Indicates that the upload of the firmware image has finished successfully.
         *
         * @param shnCapabilityFirmwareUpdate that succeeded to upload the firmware image
         */
        void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate);

        /**
         * Indicates that deploying the firmware image has failed.
         *
         * @param shnCapabilityFirmwareUpdate that failed to deploy the firmware image
         * @param shnResult                   reason to fail
         */
        void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);

        /**
         * Indicates that deploying the firmware image has finished successfully. The shnResult parameter
         * provides additional information about the deploy. The peripheral documentation should provide
         * details on the meaning of this parameter.
         *
         * @param shnCapabilityFirmwareUpdate that finished the deploy of the firmware image
         * @param shnResult                   the result of the firmware deploy
         */
        void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult);
    }
}
