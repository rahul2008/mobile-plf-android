/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import com.philips.platform.uappframework.listener.ActionBarListener;

public interface EWSActionBarListener extends ActionBarListener {
    void closeButton(boolean visibility);
}
