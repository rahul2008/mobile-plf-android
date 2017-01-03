package com.philips.platform.core.events;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class SaveUserCharacteristicsEvent extends Event {

    private String userCharacteristicsUids;

    public SaveUserCharacteristicsEvent(String userCharacteristicsUids) {
        this.userCharacteristicsUids = userCharacteristicsUids;
    }

    public String getUserCharacteristicsUids() {
        return userCharacteristicsUids;
    }
}
