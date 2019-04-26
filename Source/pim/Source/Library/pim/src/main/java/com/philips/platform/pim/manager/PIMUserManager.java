package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pim.listeners.PIMUserProfileDownloadListener;
import com.philips.platform.pim.models.PIMOIDCUserProfile;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.UserProfileRequest;

import net.openid.appauth.AuthState;

import org.json.JSONException;
import org.json.JSONObject;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMUserManager {
    private PIMOIDCUserProfile pimoidcUserProfile;
    private Context context;
    private AppInfraInterface appInfraInterface;
    private LoggingInterface mLoggingInterface;
    private final String TAG = PIMUserManager.class.getSimpleName();
    private AuthState authState;

    // TODO: Deepthi Implement getuserlogged in state API.(Done)

    public void init(Context context, AppInfraInterface appInfraInterface) {
        //get Secure Storage user profile
        this.context = context;
        this.appInfraInterface = appInfraInterface;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        //TODO : Deepthi apr 15 fetch authstate from secure storage, auth state fetching is not clear. (Done)
        String subid = getSubIDFromPref();
        if (subid != null) {
            //TODO : Deepthi auth state is not required, only fetch from SS. (Done)
            authState = getAuthStateFromSecureStorage(subid);
            String userProfileJson = getUserProfileFromSecureStorage(subid);
            //TODO : Deepthi create profile using stored json obj and auth state to fill access token(Done)
            pimoidcUserProfile = new PIMOIDCUserProfile(userProfileJson, authState);
        }

        mLoggingInterface.log(DEBUG, TAG, "User  manager initialized");
    }


    void requestUserProfile(AuthState oidcAuthState, PIMUserProfileDownloadListener userProfileRequestListener) {
        UserProfileRequest userProfileRequest = new UserProfileRequest(oidcAuthState);
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        pimRestClient.invokeRequest(userProfileRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                authState = oidcAuthState;
                saveSubIDToPreference(response);
                userProfileRequestListener.onUserProfileDownloadSuccess();
                storeUserProfileToSecureStorage(response);
                storeAuthStateToSecureStorage(response, oidcAuthState);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userProfileRequestListener.onUserProfileDownloadFailed(0);
                mLoggingInterface.log(DEBUG, TAG, "error : " + error.getMessage());
            }
        });
    }

    PIMOIDCUserProfile getOIDCUserProfile() {
        return pimoidcUserProfile;
    }


    private void storeUserProfileToSecureStorage(String jsonUserProfileResponse) {
        String userInfoKey = getKeyForStoringUserInfo(jsonUserProfileResponse);
        if (userInfoKey != null) {
            boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(userInfoKey, jsonUserProfileResponse, new SecureStorageInterface.SecureStorageError());
            mLoggingInterface.log(DEBUG, TAG, "UserProfile Stored to Secure storage : " + isStored);
        } else
            mLoggingInterface.log(DEBUG, TAG, "Key is null");
    }

    private String getUserProfileFromSecureStorage(String subID){
        String userInfoKey = "UUID_" + subID + "_AuthState";
        return appInfraInterface.getSecureStorage().fetchValueForKey(userInfoKey,new SecureStorageInterface.SecureStorageError());
    }

    private void storeAuthStateToSecureStorage(String jsonUserProfileResponse, AuthState authState) {
        String authStateKey = getKeyForStoringAuthState(jsonUserProfileResponse);
        if (authStateKey != null) {
            boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(authStateKey, authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
            mLoggingInterface.log(DEBUG, TAG, "AuthState Stored to Secure storage : " + isStored);
        } else
            mLoggingInterface.log(DEBUG, TAG, "authStateKey is null");
    }

    private AuthState getAuthStateFromSecureStorage(String subID) {
        String authstatekey = "UUID_" + subID + "_AuthState";
        String authStateString = appInfraInterface.getSecureStorage().fetchValueForKey(authstatekey, new SecureStorageInterface.SecureStorageError());
        AuthState authState = null;
        if (authStateString != null) {
            try {
                authState = AuthState.jsonDeserialize(authStateString);
            } catch (JSONException e) {
               mLoggingInterface.log(DEBUG,TAG,"exception in getAuthStateFromSecureStorage : "+e.getMessage());
            }
        }
        return authState;
    }

    private String getKeyForStoringUserInfo(String userInfoJson) {
        String subID = getSubjectIDFromUserProfileJson(userInfoJson);
        if (subID == null)
            return null;
        else
            return "UUID_" + subID + "_UserInfo";
    }

    private String getKeyForStoringAuthState(String userInfoJson) {
        String subID = getSubjectIDFromUserProfileJson(userInfoJson);
        if (subID == null)
            return null;
        else
            return "UUID_" + subID + "_AuthState";
    }

    private String getSubjectIDFromUserProfileJson(String userProfileJson) {
        if (userProfileJson == null)
            return null;
        try {
            JSONObject jsonObject = new JSONObject(userProfileJson);
            return jsonObject.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Here Sub id means subject id which is equivalent to UUID
    private String getSubIDFromPref() {
        if (context == null)
            return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("PIM_PREF", Context.MODE_PRIVATE);
        return sharedPreferences.getString("com.pim.activeuuid", null);
    }

    private void saveSubIDToPreference(String userInfoJson) {
        String subID = getSubjectIDFromUserProfileJson(userInfoJson);
        if (subID != null || context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("PIM_PREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("com.pim.activeuuid", subID);
            editor.apply();
        }else{
            mLoggingInterface.log(DEBUG, TAG, "SubID or context is null");
        }
    }

    public UserLoggedInState getUserLoggedInState(){
        if(authState != null && pimoidcUserProfile != null)
            return UserLoggedInState.USER_LOGGED_IN;
        else
            return UserLoggedInState.USER_NOT_LOGGED_IN;
    }

    //TODO: Shashi, added for getting PIMOIDCUserProfile in Data Impl class.Need to Confirm with Deepthi
    public PIMOIDCUserProfile getUserProfile(){
        return pimoidcUserProfile;
    }
}
