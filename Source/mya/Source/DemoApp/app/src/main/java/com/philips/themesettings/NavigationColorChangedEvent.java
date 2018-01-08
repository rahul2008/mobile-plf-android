/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

import com.philips.platform.uid.thememanager.NavigationColor;

public class NavigationColorChangedEvent extends MessageEvent {
    private NavigationColor navigationColor;

    public NavigationColorChangedEvent(final String message, final NavigationColor navigationColor) {
        super(message);
        this.navigationColor = navigationColor;
    }

    public NavigationColor getNavigationColor() {
        return navigationColor;
    }
}
