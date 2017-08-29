/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;


import com.philips.cdp2.commlib.core.port.PortProperties;

abstract class ArraysDataPortProperties implements PortProperties {
    protected int[] transform(Integer[] inputArray) throws InvalidPortPropertiesException {
        int[] result = new int[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] == null) {
                throw new InvalidPortPropertiesException();
            }
            result[i] = inputArray[i];
        }
        return result;
    }
}
