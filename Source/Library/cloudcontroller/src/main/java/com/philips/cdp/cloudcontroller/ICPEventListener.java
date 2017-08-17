/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import com.philips.icpinterface.ICPClient;

public interface ICPEventListener {
    void onICPCallbackEventOccurred(int eventType, int status, ICPClient obj);
}
