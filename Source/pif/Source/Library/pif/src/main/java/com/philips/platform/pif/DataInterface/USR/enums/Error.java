package com.philips.platform.pif.DataInterface.USR.enums;

//TODO: Shashi, Check with Deepthi about this error name
public class Error {
    private int errCode;
    private String errDesc;

    public Error(int errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }
}
