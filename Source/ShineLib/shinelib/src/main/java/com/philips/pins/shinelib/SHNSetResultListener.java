/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Set;

/**
 * A callback used to receive the result of a request for a set. Returns {@link SHNResult} and an obtained set.
 *
 * @publicApi
 */
public interface SHNSetResultListener<T> extends ResultListener<Set<T>> {
}
