/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A callback to receive information about a request's result.
 *
 * @publicApi
 */
public interface SHNResultListener {
    void onActionCompleted(SHNResult result);
}
