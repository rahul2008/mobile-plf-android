/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

public class UserCharacteristicsRequestFailed extends Event{
    Exception mException;

    public UserCharacteristicsRequestFailed(Exception exception) {
        super();
        mException = exception;
    }

    public Exception getException() {
        return mException;
    }
}