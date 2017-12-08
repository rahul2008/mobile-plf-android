package com.philips.platform.catalogapp.events;

import com.philips.platform.uid.thememanager.ContentColor;

public class ContentTonalRangeChangedEvent extends MessageEvent {
    ContentColor contentColor;

    public ContentTonalRangeChangedEvent(final String message, final ContentColor contentColor) {
        super(message);
        this.contentColor = contentColor;
    }

    public ContentTonalRangeChangedEvent(final String message) {
        super(message);
    }

    public ContentColor getContentColor() {
        return contentColor;
    }
}
