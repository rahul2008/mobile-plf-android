/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class NullEventException extends RuntimeException {

    private static final long serialVersionUID = -1493290864496896236L;

    public NullEventException() {
        super("Passed Event is not valid");
    }
}
