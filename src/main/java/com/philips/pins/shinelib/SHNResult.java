package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 02/03/15.
 */
public enum SHNResult {
    SHNOk,
    SHNUnknownLogSyncRecordType,
    SHNAborted,
    SHNUnsupportedOperation,
    SHNOperationFailed,
    SHNUserNotAuthorized,
    // error codes
    SHNErrorInvalidParameter,
    SHNErrorWhileParsing,
    SHNTimeoutError,
    SHNInvalidStateError,
    SHNInvalidResponseError,
    SHNResponseIncompleteError,
    SHNServiceUnavailableError,
    SHNLostConnectionError,
    SHNUnsupportedDataTypeError,
    SHNAssociationError,
    SHNLogSyncBufferFormatError,
    SHNUnknownDeviceTypeError,

    SHNErrorBluetoothDisabled,
    SHNErrorUserConfigurationIncomplete,
    SHNErrorUserConfigurationInvalid,
    SHNErrorProcedureAlreadyInProgress,
    SHNErrorReceptionInterrupted
}
