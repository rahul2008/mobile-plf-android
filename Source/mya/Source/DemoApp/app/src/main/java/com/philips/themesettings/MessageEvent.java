/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

public class MessageEvent {
    public MessageEvent(final String message) {
        this.settings = message;
    }

    private String settings;

    public String getMessage() {
        return settings;
    }
}
