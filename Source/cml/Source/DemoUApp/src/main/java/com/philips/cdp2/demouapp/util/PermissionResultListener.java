/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.util;

public interface PermissionResultListener {
    void onPermissionGranted();
    void onPermissionDenied();
}
