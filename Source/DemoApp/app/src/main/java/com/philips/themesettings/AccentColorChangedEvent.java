/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.themesettings;

import com.philips.platform.uid.thememanager.AccentRange;

public class AccentColorChangedEvent extends MessageEvent {

    private final AccentRange accentRange;

    public AccentColorChangedEvent(final String message, final AccentRange accentRange) {
        super(message);
        this.accentRange = accentRange;
    }

    public AccentRange getAccentRange() {
        return accentRange;
    }
}
