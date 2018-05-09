/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
