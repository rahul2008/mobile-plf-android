/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class StateIdNotSetException extends RuntimeException {

    private static final long serialVersionUID = 3331247819494979016L;

    public StateIdNotSetException() {
        super("There is no State Id for the passed State");
    }
}
