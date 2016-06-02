package com.philips.platform.appinfra.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.AppIdentityInterface;
import com.philips.platform.appinfra.servicediscovery.AppIdentityManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIndentityDemoPage extends AppCompatActivity{

    AppIdentityInterface mappIdentityinterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfra appInfra = AppInfraApplication.gAppInfra;
        mappIdentityinterface = appInfra.getAppIdentity();
        try {
            JSONObject obj = new JSONObject(mappIdentityinterface.loadJSONFromAsset());
            String str1= obj.getString("micrositeId");
            String str2 = obj.getString("sector");

            Log.i("Obj tag1", str1);
            Log.i("Obj tag2", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        if(countryCode == null){

        }
    }
}
