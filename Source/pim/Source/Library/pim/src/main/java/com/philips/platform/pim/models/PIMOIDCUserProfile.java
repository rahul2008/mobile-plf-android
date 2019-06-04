package com.philips.platform.pim.models;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

/**
 * Class to hold user info
 */
public class PIMOIDCUserProfile {

    private final String TAG = PIMOIDCUserProfile.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private HashMap<String, Object> mUserProfileMap;
    private AuthState authState;

    public PIMOIDCUserProfile(String userProfileJson, AuthState authState) {
        this.authState = authState;
        mUserProfileMap = new HashMap<>();
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        parseUserProfileJsonDataToMap(userProfileJson);
    }

    /**
     *
     * Method to get user profile based on key list
     * @param keyList list of keys
     * @return user info based on key list
     */
    public HashMap<String, Object> fetchUserDetails(ArrayList<String> keyList) {
        HashMap<String,Object> userDetailsMap = new HashMap<>();
        for (String key:
             keyList) {
            if(mUserProfileMap.containsKey(key))
                userDetailsMap.put(key,mUserProfileMap.get(key));
        }
        return null;
    }

    /**
     * Parse json user profile response to map
     * @param userProfleJson response received on request user profile
     */
    private void parseUserProfileJsonDataToMap(String userProfleJson) {
        if(userProfleJson == null){
            mLoggingInterface.log(DEBUG,TAG,"User Profile is not available");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(userProfleJson);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                mUserProfileMap.put(key, jsonObject.get(key));
            }

            if(authState != null){
                mUserProfileMap.put(UserDetailConstants.ACCESS_TOKEN,authState.getAccessToken());
            }
        } catch (JSONException e) {
           mLoggingInterface.log(DEBUG,TAG,"parseUserProfileDataToMap : Exception in fetching User Details ");
        }
    }
}
