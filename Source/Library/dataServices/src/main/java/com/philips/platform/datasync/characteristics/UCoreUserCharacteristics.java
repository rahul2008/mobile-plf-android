/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.utils.DSLog;

import java.util.List;

public class UCoreUserCharacteristics {
    private List<UCoreCharacteristics> characteristics;

    public void setCharacteristics(List<UCoreCharacteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<UCoreCharacteristics> getCharacteristics() {
        return characteristics;
    }

    @Override
    public String toString() {
        DSLog.i(DSLog.LOG, "Inder = UCoreUserCharacteristics =" + characteristics.get(0).getCharacteristics().get(0).toString());
        return characteristics.get(0).toString();
    }
}