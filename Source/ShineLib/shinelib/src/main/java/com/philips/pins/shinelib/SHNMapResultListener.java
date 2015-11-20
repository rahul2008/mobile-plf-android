/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Map;

public interface SHNMapResultListener<K, V> {
    void onActionCompleted(Map<K, V> value, SHNResult result);
}
