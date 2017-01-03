package com.philips.platform.core.events;

import java.util.List;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class UserCharacteristicsToBackendEvent extends Event {

    private List<String> articleUid;

    public UserCharacteristicsToBackendEvent(List<String> articleUid) {
        this.articleUid = articleUid;
    }

    public List<String> getArticleUid() {
        return articleUid;
    }
}