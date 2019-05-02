package com.philips.platform.pim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;
import java.util.HashMap;


public class PIMLaunchInput extends UappLaunchInput {
    public static final String USERINFO = "userinfo";
    private String TAG = PIMLaunchInput.class.getSimpleName();

    private ArrayList<String> pimCustomClaims;

    String getCustomClaims() {
        if (pimCustomClaims == null) return null;
        HashMap<String, String> claimMap = new HashMap<>();
        for (String claim : pimCustomClaims) {
            claimMap.put(claim, "null");
        }
        HashMap<String, HashMap<String, String>> customClaim = new HashMap<>();
        customClaim.put(USERINFO, claimMap);
        Gson prettyGson = new GsonBuilder().create();
        String prettyJson = prettyGson.toJson(customClaim);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "PIM_KEY_CUSTOM_CLAIMS: HashMap string" + prettyJson);
        return prettyJson;
    }

    public void setCustomClaims(ArrayList<String> pimCustomClaims) {
        this.pimCustomClaims = pimCustomClaims;
    }

}
