/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class NoEventFoundException extends RuntimeException {


    private static final long serialVersionUID = -3607974490392199565L;

    public NoEventFoundException() {
        super("No Event found with that Id");
    }
}
