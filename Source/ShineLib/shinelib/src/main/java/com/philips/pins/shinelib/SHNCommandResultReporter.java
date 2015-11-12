package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 05/05/15.
 */
public interface SHNCommandResultReporter {
    void reportResult(SHNResult shnResult, byte[] data);
}
