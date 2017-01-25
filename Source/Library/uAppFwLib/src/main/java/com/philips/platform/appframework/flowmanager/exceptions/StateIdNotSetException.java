/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class StateIdNotSetException extends RuntimeException {

    public StateIdNotSetException() {
        super("No State id set on constructor");
    }
}
