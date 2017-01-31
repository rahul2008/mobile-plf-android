/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

public interface FirmwarePortListener {
    void onProgressUpdated(FirmwarePortProgressType type, int progress);

    void onDownloadingFailed(FirmwarePortException exception);

    void onDownloadingFinished();

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
