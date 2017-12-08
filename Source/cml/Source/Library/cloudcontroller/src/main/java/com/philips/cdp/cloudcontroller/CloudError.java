/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import com.philips.icpinterface.data.Errors;

public enum CloudError {
    SUCCESS(Errors.SUCCESS),
    INVALID_PARAM(Errors.INVALID_PARAM),
    PLATFORM_OS(Errors.PLATFORM_OS),
    AUTHENTICATION_FAILED(Errors.AUTHENTICATION_FAILED),
    RESUME_NOT_SUPPORTED(Errors.RESUME_NOT_SUPPORTED),
    NETWORK_NOT_AVAILABLE(Errors.NETWORK_NOT_AVAILABLE),
    SOCKET_CREATION_FAILED(Errors.SOCKET_CREATION_FAILED),
    SEND_FAILED(Errors.SEND_FAILED),
    RECEIVE_FAILED(Errors.RECEIVE_FAILED),
    SERVER_ERROR(Errors.SERVER_ERROR),
    UNSUPPORTED_ERROR(Errors.UNSUPPORTED_ERROR),
    DOWNLOAD_ABORTED(Errors.DOWNLOAD_ABORTED),
    CRC_CHECK_FAILED(Errors.CRC_CHECK_FAILED),
    DOWNLOAD_NOT_IN_PROGRESS(Errors.DOWNLOAD_NOT_IN_PROGRESS),
    SIZE_OVERFLOW(Errors.SIZE_OVERFLOW),
    SIZE_MSG_ENCODE(Errors.SIZE_MSG_ENCODE),
    SIZE_MSG_DECODE(Errors.SIZE_MSG_DECODE),
    GENERAL_ERROR(Errors.GENERAL_ERROR),
    OPERATION_FAILED(Errors.OPERATION_FAILED),
    INVALID_REQUEST(Errors.INVALID_REQUEST),
    NEED_MORE_DATA_FOR_UPLOAD(Errors.NEED_MORE_DATA_FOR_UPLOAD),
    DATA_PARTIALLY_UPLOADED(Errors.DATA_PARTIALLY_UPLOADED),
    NOT_PROVISIONED(Errors.NOT_PROVISIONED),
    ERROR_NETWORK_GENERAL(Errors.ERROR_NETWORK_GENERAL),
    SERVER_NOT_REACHABLE(Errors.SERVER_NOT_REACHABLE),
    CONNECT_TIMEDOUT(Errors.CONNECT_TIMEDOUT),
    THREAD_ALREADY_RUNNING(Errors.THREAD_ALREADY_RUNNING),
    DATA_UPLOAD_ABORTED(Errors.DATA_UPLOAD_ABORTED),
    UPLOAD_NOT_IN_PROGRESS(Errors.UPLOAD_NOT_IN_PROGRESS),
    FORBIDDEN_ERROR(Errors.FORBIDDEN_ERROR),
    DECRYPTION_FAILED(Errors.DECRYPTION_FAILED),
    REQUEST_PENDING(Errors.REQUEST_PENDING),
    UNKNOWN(999);

    public final int errorCode;

    CloudError(final int errorCode) {
        this.errorCode = errorCode;
    }

    public static CloudError fromErrorCode(final int errorCode) {
        for (final CloudError error : values()) {
            if (error.errorCode == errorCode) {
                return error;
            }
        }
        return UNKNOWN;
    }
}