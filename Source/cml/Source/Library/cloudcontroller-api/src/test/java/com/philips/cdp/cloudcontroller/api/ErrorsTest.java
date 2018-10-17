/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ErrorsTest {
    @Test
    public void errorValuesShouldBeEqualToTheOnesProvidedInTheICPClientLibrary() throws Exception {
        assertEquals(Errors.SUCCESS, 0);
        assertEquals(Errors.INVALID_PARAM, 1);
        assertEquals(Errors.PLATFORM_OS, 2);
        assertEquals(Errors.AUTHENTICATION_FAILED, 3);
        assertEquals(Errors.RESUME_NOT_SUPPORTED, 4);
        assertEquals(Errors.NETWORK_NOT_AVAILABLE, 5);
        assertEquals(Errors.SOCKET_CREATION_FAILED, 6);
        assertEquals(Errors.SEND_FAILED, 7);
        assertEquals(Errors.RECEIVE_FAILED, 8);
        assertEquals(Errors.SERVER_ERROR, 9);
        assertEquals(Errors.UNSUPPORTED_ERROR, 10);
        assertEquals(Errors.DOWNLOAD_ABORTED, 11);
        assertEquals(Errors.CRC_CHECK_FAILED, 12);
        assertEquals(Errors.DOWNLOAD_NOT_IN_PROGRESS, 13);
        assertEquals(Errors.SIZE_OVERFLOW, 14);
        assertEquals(Errors.SIZE_MSG_ENCODE, 15);
        assertEquals(Errors.SIZE_MSG_DECODE, 16);
        assertEquals(Errors.GENERAL_ERROR, 17);
        assertEquals(Errors.OPERATION_FAILED, 18);
        assertEquals(Errors.INVALID_REQUEST, 19);
        assertEquals(Errors.NEED_MORE_DATA_FOR_UPLOAD, 20);
        assertEquals(Errors.DATA_PARTIALLY_UPLOADED, 21);
        assertEquals(Errors.NOT_PROVISIONED, 22);
        assertEquals(Errors.ERROR_NETWORK_GENERAL, 23);
        assertEquals(Errors.SERVER_NOT_REACHABLE, 24);
        assertEquals(Errors.CONNECT_TIMEDOUT, 25);
        assertEquals(Errors.THREAD_ALREADY_RUNNING, 26);
        assertEquals(Errors.DATA_UPLOAD_ABORTED, 27);
        assertEquals(Errors.UPLOAD_NOT_IN_PROGRESS, 28);
        assertEquals(Errors.FORBIDDEN_ERROR, 29);
        assertEquals(Errors.DECRYPTION_FAILED, 30);
        assertEquals(Errors.REQUEST_PENDING, 31);
        assertEquals(Errors.REQUEST_NOT_ACCEPTED_AUTH_INPROGRESS, 32);
    }
}
