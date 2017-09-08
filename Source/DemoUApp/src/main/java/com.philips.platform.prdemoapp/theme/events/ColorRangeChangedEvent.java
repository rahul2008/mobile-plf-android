package com.philips.platform.prdemoapp.theme.events;

import com.philips.platform.uid.thememanager.ColorRange;

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
