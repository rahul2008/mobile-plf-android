package com.philips.platform.catalogapp.events;

import com.philips.platform.uit.thememanager.ContentColor;

public class TonalRangeChangedEvent extends MessageEvent {
    ContentColor contentColor;

    public TonalRangeChangedEvent(final String message, final ContentColor contentColor) {
        super(message);
        this.contentColor = contentColor;
    }

    public TonalRangeChangedEvent(final String message) {
        super(message);
    }

    public ContentColor getContentColor() {
        return contentColor;
    }
}
