/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

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
