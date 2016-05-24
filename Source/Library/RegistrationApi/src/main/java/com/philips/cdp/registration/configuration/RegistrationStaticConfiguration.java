
package com.philips.cdp.registration.configuration;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;

public class RegistrationStaticConfiguration extends RegistrationBaseConfiguration {


    private static volatile RegistrationStaticConfiguration registrationConfiguration;


    private RegistrationStaticConfiguration() {

    }

    public static RegistrationStaticConfiguration getInstance() {
        if (registrationConfiguration == null) {
            synchronized (RegistrationStaticConfiguration.class) {
                if (registrationConfiguration == null) {
                    registrationConfiguration = new RegistrationStaticConfiguration();
                }
            }
        }
        return registrationConfiguration;
    }


    public boolean parseConfigurationJson(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        try {

            JSONObject responseJSonObj = new JSONObject(URLDecoder.decode(convertStreamToString(assetManager.open(path)), "UTF-8"));
            ConfigurationParser configurationParser = new ConfigurationParser();
            configurationParser.parse(responseJSonObj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


}
