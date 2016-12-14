package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseCharacteristicsUpdateRequest extends Event {

    private Characteristics characteristics;

    public DatabaseCharacteristicsUpdateRequest(Characteristics characteristics) {
        this.characteristics = characteristics;
    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }
}
