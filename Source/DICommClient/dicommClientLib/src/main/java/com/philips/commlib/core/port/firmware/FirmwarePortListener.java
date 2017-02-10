/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

public interface FirmwarePortListener {
    void onProgressUpdated(FirmwarePortProgressType type, int progress);

    void onDownloadFailed(FirmwarePortException exception);

    void onDownloadFinished();

    void onFirmwareAvailable(String version);

    void onDeployFailed(FirmwarePortException exception);

    void onDeployFinished();

    enum FirmwarePortProgressType {
        DOWNLOADING,
        CHECKING
    }

    class FirmwarePortException extends Exception {
        public FirmwarePortException(String message) {
            super(message);
        }

        public FirmwarePortException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
