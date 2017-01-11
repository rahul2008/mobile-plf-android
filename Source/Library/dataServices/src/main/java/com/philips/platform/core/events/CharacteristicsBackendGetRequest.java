/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;

public class CharacteristicsBackendGetRequest extends Event {

    private Characteristics characteristics;

    public CharacteristicsBackendGetRequest(Characteristics characteristics) {
        this.characteristics = characteristics;
    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }
}
