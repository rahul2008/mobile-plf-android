/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Map;

/**
 * A callback used to receive the result of a request for a map. Returns {@link SHNResult} and an obtained map.
 */
public interface SHNMapResultListener<K, V> extends ResultListener<Map<K, V>> {
}
