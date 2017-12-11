/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.error;

import com.philips.cdp.dicommclient.request.Error;

public class PortErrorException extends Exception {
    private final Error error;

    public PortErrorException(Error error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
