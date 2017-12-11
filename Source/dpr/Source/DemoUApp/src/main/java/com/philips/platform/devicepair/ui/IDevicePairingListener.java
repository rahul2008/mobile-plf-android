/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import java.util.List;

public interface IDevicePairingListener {
    void onGetPairedDevices(List<String> pairedDeviceList);

    void onDevicePaired(String pairedDeviceID);

    void onDeviceUnPaired(String unPairedDeviceID);

    void onConsentNotAccepted();

    void onConsentsAccepted();

    void onProfileNotCreated();

    void onProfileCreated();

    void onInternetError();

    void onError(String errorMessage);

    void onAccessTokenExpiry();
}
