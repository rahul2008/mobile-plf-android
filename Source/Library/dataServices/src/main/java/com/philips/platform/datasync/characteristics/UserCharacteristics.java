package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;

import java.util.List;

/**
 * Created by indrajitkumar on 1/2/17.
 */
public class UserCharacteristics {
    private List<Characteristics> characteristics;

    public UserCharacteristics(@NonNull final List<Characteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<Characteristics> getCharacteristics() {
        return characteristics;
    }
}
