/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.catalogapp.events;

public class InfoMessageDismissedEvent extends MessageEvent {
    public InfoMessageDismissedEvent(final String message) {
        super(message);
    }
}
