/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class LocalManager implements LocalInterface {

    AppInfra mAppInfra;
    Context context;
    String mCountry;
    private static final String COUNTRY_URL = "";

    public LocalManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }


    public String getlanguage(){
        if(Locale.getDefault().getLanguage() != null){
            return Locale.getDefault().getLanguage();
        }else{
            return null;
        }
    }
    @Override
    public String  getCountry() {
        SharedPreferences pref = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);
        if(mCountry == null){
            mCountry = pref.getString(RequestManager.COUNTRY_NAME, null);
            Log.i("Country", " "+mCountry);
            if(mCountry!= null)
                return mCountry;

        }
        if(mCountry== null){
            SharedPreferences.Editor editor = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE).edit();
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    mCountry = simCountry.toLowerCase(Locale.US);

                    editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                    editor.commit();
                    if(mCountry!= null)
                    return mCountry;
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        mCountry= networkCountry.toLowerCase(Locale.US);
                        editor.putString(RequestManager.COUNTRY_NAME, mCountry);
                        editor.commit();
                        if(mCountry!= null)
                        return mCountry;
                    }
                }
            } catch (Exception e) {
            }
        }

//        if(mCountry == null){
//
//            new ServiceDiscoveryManager(mAppInfra).refresh((new ServiceDiscoveryInterface.OnRefreshListener() {
//                @Override
//                public void onError(ERRORVALUES error, String message) {
//
//                }
//                @Override
//                public void onSuccess() {
//                    SharedPreferences pref = context.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);
//                    mCountry = pref.getString(RequestManager.COUNTRY_NAME, null);
//                    Log.i("Retrieved Country", " "+mCountry);
//
//                }
//            }));
//            return mCountry;
//        }
        return mCountry;
    }


}
