/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

/**
 *
 * @since 1903
 */
public class UserDataInterfaceException extends Exception {

    Error error;

    public UserDataInterfaceException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
