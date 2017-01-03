package com.philips.platform.core.events;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class SendUserCharacteristicsToBackendResponseEvent extends Event {

    private String articleUids;

    public SendUserCharacteristicsToBackendResponseEvent(String articleUids) {
        this.articleUids = articleUids;
    }

    public String getArticleUids() {
        return articleUids;
    }
}