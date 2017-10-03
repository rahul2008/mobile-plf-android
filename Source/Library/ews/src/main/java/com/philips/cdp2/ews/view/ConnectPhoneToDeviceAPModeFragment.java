/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.viewmodel.ConnectPhoneToDeviceAPModeViewModel;

public abstract class ConnectPhoneToDeviceAPModeFragment<VM extends ConnectPhoneToDeviceAPModeViewModel, T extends ViewDataBinding> extends EWSBaseFragment<T> {

    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;
    private boolean pendingPermissionResultRequest;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            pendingPermissionResultRequest = getViewModel().areAllPermissionsGranted(grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pendingPermissionResultRequest) {
            pendingPermissionResultRequest = false;
            getViewModel().connectPhoneToDeviceHotspotWifi();
        }
    }

    abstract VM getViewModel();
}