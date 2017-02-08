/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

public interface FirmwarePortListener {
    void onProgressUpdated(FirmwarePortProgressType type, int progress);

    void onDownloadFailed(FirmwarePortException exception);

    void onDownloadFinished();

    void onFirmwareAvailable(String version);

    void onDeployFailed(FirmwarePortException exception);

    void onDeployFinished();

    enum FirmwarePortProgressType {
        DOWNLOADING,
        CHECKING,
        DEPLOYING
    }

    class FirmwarePortException extends Exception {
    }
}
