package com.philips.platform.appinfra.servicediscovery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class LocalManager extends Activity implements LocalInterface {

    AppInfra mAppInfra;
    Context context;
    String s;
    int MY_PERMISSIONS_REQUEST_READ_PERMISSION = 1;

    public LocalManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void checkpermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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

//        checkpermissions();

        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                s = simCountry.toLowerCase(Locale.US);
                return s;
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    s= networkCountry.toLowerCase(Locale.US);
                    return s;
                }
            }
        } catch (Exception e) {
        }
//        if(s != null){
//            return s;
//        }else{
//            return null;
//        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        final String simCountry = tm.getSimCountryIso();
                        if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                             s = simCountry.toLowerCase(Locale.US);
                            return;
                        } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                            String networkCountry = tm.getNetworkCountryIso();
                            if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                                s= networkCountry.toLowerCase(Locale.US);
                                return;
                            }
                        }
                    } catch (Exception e) {
                    }
                    return;

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
