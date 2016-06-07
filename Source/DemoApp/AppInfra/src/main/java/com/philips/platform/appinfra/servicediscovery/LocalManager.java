package com.philips.platform.appinfra.servicediscovery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        SharedPreferences pref = context.getSharedPreferences("PrefNAme", context.MODE_PRIVATE);
        if(mCountry == null){
            mCountry = pref.getString("COUNTRY_NAME", null);
            Log.i("Retried Country", " "+mCountry);
        }
        if(mCountry== null){
            SharedPreferences.Editor editor = context.getSharedPreferences("PrefNAme", context.MODE_PRIVATE).edit();
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    mCountry = simCountry.toLowerCase(Locale.US);

                    editor.putString("COUNTRY_NAME", mCountry);
                    editor.commit();
                    return mCountry;
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { //
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        mCountry= networkCountry.toLowerCase(Locale.US);
                        editor.putString("COUNTRY_NAME", mCountry);
                        editor.commit();
                        return mCountry;
                    }
                }
            } catch (Exception e) {
            }
        }

        if(mCountry == null){
        new RequestManager(context).execute("https://tst.philips.com/api/v1/discovery/b2c/12345?locale=en");
        }
        return null;
    }


}
