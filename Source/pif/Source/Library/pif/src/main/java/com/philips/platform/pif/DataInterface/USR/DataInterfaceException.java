package com.philips.platform.pif.DataInterface.USR;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public class DataInterfaceException extends Exception {
    Error error;
    public DataInterfaceException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
