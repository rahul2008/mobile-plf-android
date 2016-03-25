package com.philips.cdp.coppa.registration;

import android.content.Context;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.engage.JREngageError;

import org.json.JSONObject;

/**
 * Created by 310202337 on 3/25/2016.
 */
 class CoppaConsentUpdater {

    private Context mContext;
    private String LOG_TAG = "Coppa";
    CoppaConsentUpdater(final Context context){
        mContext = context;
    }
    public void updateCoppaConsentStatus(){
        if(Jump.getSignedInUser() != null){
            CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
            JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);

        }else{
            Log.e(LOG_TAG, "Trying to update consent status when no user is logged in");
        }

    }

    public void updateCoppaConsentConfirmationStatus(){
        if(Jump.getSignedInUser() != null){


        }else{
            Log.e(LOG_TAG, "Trying to update consent confirmation when no user is logged in");
        }

    }

}
