/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class JsonStructureException extends RuntimeException {

    private static final long serialVersionUID = -5584342919698461367L;

    public JsonStructureException() {
        super("The Json structure is wrong");
    }
}
