/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CloudErrorTest {

    @Test
    public void cloudErrorValuesShouldBeEqualToTheOnesProvidedInTheICPClientLibrary() throws Exception {
        assertEquals(CloudError.SUCCESS.errorCode, 0);
        assertEquals(CloudError.INVALID_PARAM.errorCode, 1);
        assertEquals(CloudError.PLATFORM_OS.errorCode, 2);
        assertEquals(CloudError.AUTHENTICATION_FAILED.errorCode, 3);
        assertEquals(CloudError.RESUME_NOT_SUPPORTED.errorCode, 4);
        assertEquals(CloudError.NETWORK_NOT_AVAILABLE.errorCode, 5);
        assertEquals(CloudError.SOCKET_CREATION_FAILED.errorCode, 6);
        assertEquals(CloudError.SEND_FAILED.errorCode, 7);
        assertEquals(CloudError.RECEIVE_FAILED.errorCode, 8);
        assertEquals(CloudError.SERVER_ERROR.errorCode, 9);
        assertEquals(CloudError.UNSUPPORTED_ERROR.errorCode, 10);
        assertEquals(CloudError.DOWNLOAD_ABORTED.errorCode, 11);
        assertEquals(CloudError.CRC_CHECK_FAILED.errorCode, 12);
        assertEquals(CloudError.DOWNLOAD_NOT_IN_PROGRESS.errorCode, 13);
        assertEquals(CloudError.SIZE_OVERFLOW.errorCode, 14);
        assertEquals(CloudError.SIZE_MSG_ENCODE.errorCode, 15);
        assertEquals(CloudError.SIZE_MSG_DECODE.errorCode, 16);
        assertEquals(CloudError.GENERAL_ERROR.errorCode, 17);
        assertEquals(CloudError.OPERATION_FAILED.errorCode, 18);
        assertEquals(CloudError.INVALID_REQUEST.errorCode, 19);
        assertEquals(CloudError.NEED_MORE_DATA_FOR_UPLOAD.errorCode, 20);
        assertEquals(CloudError.DATA_PARTIALLY_UPLOADED.errorCode, 21);
        assertEquals(CloudError.NOT_PROVISIONED.errorCode, 22);
        assertEquals(CloudError.ERROR_NETWORK_GENERAL.errorCode, 23);
        assertEquals(CloudError.SERVER_NOT_REACHABLE.errorCode, 24);
        assertEquals(CloudError.CONNECT_TIMEDOUT.errorCode, 25);
        assertEquals(CloudError.THREAD_ALREADY_RUNNING.errorCode, 26);
        assertEquals(CloudError.DATA_UPLOAD_ABORTED.errorCode, 27);
        assertEquals(CloudError.UPLOAD_NOT_IN_PROGRESS.errorCode, 28);
        assertEquals(CloudError.FORBIDDEN_ERROR.errorCode, 29);
        assertEquals(CloudError.DECRYPTION_FAILED.errorCode, 30);
        assertEquals(CloudError.REQUEST_PENDING.errorCode, 31);
        assertEquals(CloudError.REQUEST_NOT_ACCEPTED_AUTH_INPROGRESS.errorCode, 32);
        assertEquals(CloudError.UNKNOWN.errorCode, 999);
    }
}
