/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.UserCharacteristics;

public class CharacteristicsBackendGetRequest extends Event {

    private UserCharacteristics userCharacteristics;

    public CharacteristicsBackendGetRequest(UserCharacteristics userCharacteristics) {
        this.userCharacteristics = userCharacteristics;
    }

    public UserCharacteristics getUserCharacteristics() {
        return userCharacteristics;
    }
}
