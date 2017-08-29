/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

public enum ConnectivityDeviceType {
    POWER_SLEEP(1),
    REFERENCE_NODE(2);
    private int value;
    private ConnectivityDeviceType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}