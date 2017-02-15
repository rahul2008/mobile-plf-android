/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.exceptions;

public class ConditionIdNotSetException extends RuntimeException {

    private static final long serialVersionUID = -9041759584034509458L;

    public ConditionIdNotSetException() {
        super("There is no Condition Id for the passed Condition");
    }
}
