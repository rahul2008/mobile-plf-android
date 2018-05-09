/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

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
