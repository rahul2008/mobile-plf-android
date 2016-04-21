package com.philips.pins.shinelib.dicommsupport;

enum StatusCode {
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
    ProtocolViolation(14);

    private byte status;

    StatusCode(int status) {
        this.status = (byte) status;
    }

    public byte getByte() {
        return status;
    }

    public static StatusCode fromByte(byte type) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getByte() == type) {
                return statusCode;
            }
        }

        return null;
    }
}
