/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * A callback used to receive the result of a request for an integer. Returns {@link SHNResult} and an obtained integer number.
 *
 * @publicApi
 */
public interface SHNIntegerResultListener {
    void onActionCompleted(int value, SHNResult result);
}
