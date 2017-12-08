/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

/**
 * Listener interface to notify an object about events in the {@link FirmwarePort}
 * @publicApi
 */
public interface FirmwarePortListener {

    /**
     * Called when the appliance is in the process of checking the firmware's validity.
     * @param progress int The progress (percentage) of checking the firmware's validity.
     */
    void onCheckingProgress(int progress);

    /**
     * Called when the appliance is in the process of downloading new firmware.
     * @param progress int The progress (percentage) of downloading the new firmware.
     */
    void onDownloadProgress(int progress);

    /**
     * Called when downloading new firmware to the appliance did not complete successfully.
     * @param exception FirmwarePortException The exception that occurred.
     */
    void onDownloadFailed(FirmwarePortException exception);

    /**
     * Called when downloading new firmware to the appliance completed successfully.
     */
    void onDownloadFinished();

    /**
     * Called when a firmware upgrade is available.
     * @param version String The new version to upgrade to.
     */
    void onFirmwareAvailable(String version);

    /**
     * Called when deploying the newly downloaded and checked firmware has failed.
     * @param exception FirmwarePortException The exception that occurred.
     */
    void onDeployFailed(FirmwarePortException exception);

    /**
     * Called when deploying the newly downloaded and checked firmwas was successful.
     */
    void onDeployFinished();

    /**
     * General exception for firmware related errors.
     */
    class FirmwarePortException extends Exception {
        /**
         * Constructs a FirmwarePortException
         * @param message String The error message
         */
        public FirmwarePortException(String message) {
            super(message);
        }

        /**
         * Constructs a FirmwarePortException
         * @param message String The error message
         * @param cause Throwable The cause of this exception
         */
        public FirmwarePortException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
