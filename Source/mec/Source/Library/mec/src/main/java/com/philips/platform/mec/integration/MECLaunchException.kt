package com.philips.platform.mec.integration

class MECLaunchException(message:String,errorCode:Int) : Exception(message){

    companion object {
        const val ERROR_CODE_NOT_LOGGED_IN = 2000
        const val ERROR_CODE_NO_INTERNET = 2001
    }
}