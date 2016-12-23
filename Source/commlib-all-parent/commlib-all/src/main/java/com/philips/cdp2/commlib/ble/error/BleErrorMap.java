/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.error;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.pins.shinelib.dicommsupport.StatusCode;

import java.util.HashMap;
import java.util.Map;

/**
 * The type BleErrorMap.
 * <p>
 * This provides a mapping between all BlueLib {@link StatusCode} values to
 * request {@link Error} values.
 */
public final class BleErrorMap {
    private static final Map<StatusCode, Error> MAPPING = new HashMap<StatusCode, Error>() {{
        put(StatusCode.NotUnderstood, Error.NOT_UNDERSTOOD);
        put(StatusCode.OutOfMemory, Error.OUT_OF_MEMORY);
        put(StatusCode.NoSuchOperation, Error.NO_SUCH_OPERATION);
        put(StatusCode.NoSuchPort, Error.NO_SUCH_PORT);
        put(StatusCode.NoSuchProperty, Error.NO_SUCH_PROPERTY);
        put(StatusCode.VersionNotSupported, Error.VERSION_NOT_SUPPORTED);
        put(StatusCode.NotImplemented, Error.NOT_IMPLEMENTED);
        put(StatusCode.NoSuchProduct, Error.NO_SUCH_PRODUCT);
        put(StatusCode.PropertyAlreadyExists, Error.PROPERTY_ALREADY_EXISTS);
        put(StatusCode.NoSuchMethod, Error.NO_SUCH_METHOD);
        put(StatusCode.WrongParameters, Error.WRONG_PARAMETERS);
        put(StatusCode.InvalidParameter, Error.INVALID_PARAMETER);
        put(StatusCode.NotSubscribed, Error.NOT_SUBSCRIBED);
        put(StatusCode.ProtocolViolation, Error.PROTOCOL_VIOLATION);
        put(StatusCode.Unknown, Error.UNKNOWN);
    }};

    private BleErrorMap() {
    }

    /**
     * Gets error by status code.
     *
     * @param statusCode the status code
     * @return the error by status code
     * <p>
     * If a mapping exists, this will return the corresponding error value,
     * or {@link Error#UNKNOWN} if none is found.
     */
    public static Error getErrorByStatusCode(StatusCode statusCode) {
        return MAPPING.containsKey(statusCode) ? MAPPING.get(statusCode) : Error.UNKNOWN;
    }
}

