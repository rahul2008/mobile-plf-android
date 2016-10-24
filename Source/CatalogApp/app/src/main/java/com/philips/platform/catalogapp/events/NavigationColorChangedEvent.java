package com.philips.platform.catalogapp.events;

import com.philips.platform.uit.thememanager.NavigationColor;

public class NavigationColorChangedEvent extends MessageEvent {
    private NavigationColor navigationColor;

    public NavigationColorChangedEvent(final String message, final NavigationColor navigationColor) {
        super(message);
        this.navigationColor = navigationColor;
    }

    public NavigationColorChangedEvent(final String message) {
        super(message);
    }

    public NavigationColor getNavigationColor() {
        return navigationColor;
    }
}
