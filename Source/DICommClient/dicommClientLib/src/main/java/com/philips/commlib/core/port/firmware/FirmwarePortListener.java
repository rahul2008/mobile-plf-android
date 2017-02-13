/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

public interface FirmwarePortListener {
    void onProgressUpdated(FirmwarePortState state, int progress);

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
