/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * Possible requests' outcomes.
 *
 * @publicApi
 */
public enum SHNResult {
    SHNOk,
    SHNAborted,
    // error codes
    SHNErrorUnknownLogSyncRecordType,
    SHNErrorUnsupportedOperation,
    SHNErrorOperationFailed,
    SHNErrorUserNotAuthorized,
    SHNErrorInvalidParameter,
    SHNErrorWhileParsing,
    SHNErrorTimeout,
    SHNErrorInvalidState,
    SHNErrorInvalidResponse,
    SHNErrorResponseIncomplete,
    SHNErrorServiceUnavailable,
    SHNErrorConnectionLost,
    SHNErrorAssociationFailed,
    SHNErrorLogSyncBufferFormat,
    SHNErrorUnknownDeviceType,
    SHNErrorBluetoothDisabled,
    SHNErrorUserConfigurationIncomplete,
    SHNErrorUserConfigurationInvalid,
    SHNErrorUnsupportedDataType,
    SHNErrorProcedureAlreadyInProgress,
    SHNErrorReceptionInterrupted,
    SHNErrorBondLost,
    SHNErrorUnknown
}
