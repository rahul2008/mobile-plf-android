package com.philips.platform.catalogapp.events;

import com.philips.platform.uit.thememanager.ColorRange;

public class ColorRangeChangedEvent extends MessageEvent {
    private ColorRange colorRange;

    public ColorRangeChangedEvent(final String message, final ColorRange colorRange) {
        super(message);
        this.colorRange = colorRange;
    }

    public ColorRangeChangedEvent(final String message) {
        super(message);
    }

    public ColorRange getColorRange() {
        return colorRange;
    }
}
