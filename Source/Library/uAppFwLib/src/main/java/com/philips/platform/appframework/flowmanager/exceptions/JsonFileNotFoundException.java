/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class JsonFileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4217034065087436031L;

    public JsonFileNotFoundException() {
        super("There is no Json in the given path");
    }
}
