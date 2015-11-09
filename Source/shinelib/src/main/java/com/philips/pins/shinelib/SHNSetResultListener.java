package com.philips.pins.shinelib;

import java.util.Set;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNSetResultListener <T> {
    void onActionCompleted(Set<T> value, SHNResult result);
}
