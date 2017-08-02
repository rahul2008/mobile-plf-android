/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;

class PortParameters {
    String portName;
    int productId;

    PortParameters(@NonNull String portName, int productId) {
        this.portName = portName;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        PortParameters that = (PortParameters) other;

        if (productId != that.productId) return false;
        return portName.equals(that.portName);
    }

    @Override
    public int hashCode() {
        int result = portName.hashCode();
        result = 31 * result + productId;
        return result;
    }
}
