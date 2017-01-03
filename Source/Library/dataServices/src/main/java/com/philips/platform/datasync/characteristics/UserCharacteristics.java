package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristic;

import java.util.List;

/**
 * Created by indrajitkumar on 1/2/17.
 */
public class UserCharacteristics {
    private List<Characteristic> characteristics;

    public UserCharacteristics(@NonNull final List<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }
}
