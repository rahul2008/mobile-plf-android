package com.philips.cdp2.ews.view.dialog;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

public interface OnDeviceAPModeTroubleshootListener {
    void showWrongPhoneConnectedGuidelineView();

    void showEnableAPModeGuidelineView();

    void showFullMenuGuidelineView();

    void showResetConnectionGuidelineView();

    void dismissAPModeTroubleshootingView();
}