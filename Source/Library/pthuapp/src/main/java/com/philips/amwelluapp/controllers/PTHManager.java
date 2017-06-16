package com.philips.amwelluapp.controllers;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.exception.AWSDKInstantiationException;

public class PTHManager {

    private static PTHManager pthManager;

    private PTHManager(){

    }

    public static PTHManager getInstance(){
        if(null == pthManager){
            pthManager = new PTHManager();
        }
        return pthManager;
    }

    public AWSDK getAwsdk(Context context){
        try {
            return AWSDKFactory.getAWSDK(context);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
            return null;//TODO : return error code
        }
    }
}
