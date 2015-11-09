package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 02/03/15.
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
    SHNErrorProcedureAlreadyInProgress,
    SHNErrorReceptionInterrupted
}
