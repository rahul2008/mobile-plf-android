/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;

public class CharacteristicsBackendGetRequest extends Event {

    private Characteristics userCharacteristics;

    public CharacteristicsBackendGetRequest(Characteristics userCharacteristics) {
        this.userCharacteristics = userCharacteristics;
    }

    public Characteristics getUserCharacteristics() {
        return userCharacteristics;
    }
}
