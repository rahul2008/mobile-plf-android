/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

public class DhpCommunicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DhpCommunicationException(Throwable e) {
        super(e);
    }

    public DhpCommunicationException(String dhpResponseCode, String message) {
        super("DHP responded with code: " + dhpResponseCode + ", message: " + message);
    }
}
