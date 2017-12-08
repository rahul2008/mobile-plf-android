package com.philips.platform.prdemoapp.theme.events;

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
