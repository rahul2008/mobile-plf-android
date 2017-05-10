/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

public enum StatusCode {
    NoError(0),
    NotUnderstood(1),
    OutOfMemory(2),
    NoSuchOperation(7),
    NoSuchPort(3),
    NoSuchProperty(6),
    VersionNotSupported(5),
    NotImplemented(4),
    NoSuchProduct(8),
    PropertyAlreadyExists(9),
    NoSuchMethod(10),
    WrongParameters(11),
    InvalidParameter(12),
    NotSubscribed(13),
    ProtocolViolation(14),
    Unknown(255);

    private byte diCommStatusCode;

    StatusCode(int diCommStatusCode) {
        this.diCommStatusCode = (byte) diCommStatusCode;
    }

    public byte getDiCommStatusCode() {
        return diCommStatusCode;
    }

    public static StatusCode fromDiCommStatusCode(byte diCommStatusCode) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getDiCommStatusCode() == diCommStatusCode) {
                return statusCode;
            }
        }

        return null;
    }
}
