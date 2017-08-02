/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

public interface FirmwarePortListener {
    void onCheckingProgress(int progress);

    void onDownloadProgress(int progress);

    void onDownloadFailed(FirmwarePortException exception);

    void onDownloadFinished();

    void onFirmwareAvailable(String version);

    void onDeployFailed(FirmwarePortException exception);

    void onDeployFinished();

    class FirmwarePortException extends Exception {
        public FirmwarePortException(String message) {
            super(message);
        }

        public FirmwarePortException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
