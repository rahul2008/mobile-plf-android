package com.philips.pins.shinelib;

import com.philips.pins.shinelib.datatypes.SHNData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SHNDataResultListener {
    void onActionCompleted(SHNData value, SHNResult result);
}
