package com.philips.platform.pim;

import com.google.gson.JsonObject;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.ArrayList;


public class PIMLaunchInput extends UappLaunchInput {
    public static final String USERINFO = "userinfo";
    private String TAG = PIMLaunchInput.class.getSimpleName();

    private ArrayList<String> pimCustomClaims;

    String getCustomClaims() {
        if (pimCustomClaims == null) return null;
        JsonObject object = new JsonObject();
        for (String claim : pimCustomClaims) {
            object.add(claim, null);
        }
        JsonObject userInfo = new JsonObject();
        userInfo.add(USERINFO, object);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "PIM_KEY_CUSTOM_CLAIMS: HashMap string" + userInfo.toString());
        return userInfo.toString();
    }

    public void setCustomClaims(ArrayList<String> pimCustomClaims) {
        this.pimCustomClaims = pimCustomClaims;
    }

}
