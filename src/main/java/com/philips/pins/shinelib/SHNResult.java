package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 02/03/15.
 */
public enum SHNResult {
    SHNOk,
    SHNInvalidParameterError,
    SHNErrorWhileParsing,
    SHNUnexpectedMessageError,
    SHNTimeoutError,
    SHNInvalidStateError,
    SHNResponseIncompleteError,
    SHNServiceUnavailableError,
    SHNLostConnectionError,
    SHNUnsupportedDataTypeError,
    SHNAssociationError,
    SHNLogSyncBufferFormatError,
    SHNUnknownLogSyncRecordType,
    SHNUnknownDeviceTypeError,
    SHNBluetoothDisabledError,
    SHNUnsupportedOperation
}
