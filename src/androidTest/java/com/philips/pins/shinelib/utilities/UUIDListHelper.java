package com.philips.pins.shinelib.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 09/03/15.
 */
public class UUIDListHelper {
    private List<UUID> uuidList = new ArrayList<>();
    public UUIDListHelper(int nrUUIDs) {
        for (int index = 0; index < nrUUIDs; index++) {
            uuidList.add(UUID.randomUUID());
        }
    }

    public Set<UUID> getSet() {
        Set<UUID> uuidSet = new HashSet<>();
        uuidSet.addAll(uuidList);
        return uuidSet;
    }

    public UUID[] getArray() {
        return uuidList.toArray(new UUID[uuidList.size()]);
    }

    public int getSize() {
        return uuidList.size();
    }
}
