/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class NoConditionFoundException extends Exception {
    static final long serialVersionUID = 1L;

    public NoConditionFoundException() {
        super("No Condition Found");
    }
}
