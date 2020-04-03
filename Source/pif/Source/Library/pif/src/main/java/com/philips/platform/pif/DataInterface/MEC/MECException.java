package com.philips.platform.pif.DataInterface.MEC;

public class MECException extends Exception{

    private int errorCode;

    public static int ERROR_CODE_NOT_LOGGED_IN = 2000;
    public static int ERROR_CODE_NO_INTERNET = 2001;

    public MECException(String message,int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
