/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class NoConditionFoundException extends RuntimeException {


    private static final long serialVersionUID = -2818644841378764351L;

    public NoConditionFoundException() {
        super("No Condition found with that Id");
    }
}
