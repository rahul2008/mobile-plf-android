package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
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

    public void init(Context context, AppInfraInterface appInfraInterface) {
        //get Secure Storage user profile
        this.context = context;
        this.appInfraInterface = appInfraInterface;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        //TODO : Deepthi apr 15 fetch authstate from secure storage, auth state fetching is not clear.
        String subid = getSubIDFromPref();
        if (subid != null) {
            authState = getAuthStateFromSecureStorage(subid);
            pimoidcUserProfile = new PIMOIDCUserProfile(appInfraInterface.getSecureStorage(), authState);
        }

        mLoggingInterface.log(DEBUG, TAG, "User  manager initialized");
    }


    public void requestUserProfile(AuthState oidcAuthState, PIMUserProfileDownloadListener userProfileRequestListener) {
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

    public PIMOIDCUserProfile getOIDCUserProfile() {
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

    private void storeAuthStateToSecureStorage(String jsonUserProfileResponse, AuthState authState) {
        String authStateKey = getKeyForStoringAuthState(jsonUserProfileResponse);
        if (authStateKey != null) {
            boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(authStateKey, authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
            mLoggingInterface.log(DEBUG, TAG, "AuthState Stored to Secure storage : " + isStored);
        } else
            mLoggingInterface.log(DEBUG, TAG, "authStateKey is null");
    }

    private AuthState getAuthStateFromSecureStorage(String subID) {
        String authstatekey = "UUID_" + subID + "_UserInfo";
        String authStateString = appInfraInterface.getSecureStorage().fetchValueForKey(authstatekey, new SecureStorageInterface.SecureStorageError());
        AuthState authState = null;
        if (authStateString != null) {
            try {
                authState = AuthState.jsonDeserialize(authStateString);
            } catch (JSONException e) {
                e.printStackTrace();
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

    private String getSubIDFromPref() {
        if (context == null)
            return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("PIM_PREF", Context.MODE_PRIVATE);
        return sharedPreferences.getString("com.pim.activeuuid", null);
    }

    private boolean saveSubIDToPreference(String userInfoJson) {
        String subID = getSubjectIDFromUserProfileJson(userInfoJson);
        if (subID == null || context == null)
            return false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("PIM_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.pim.activeuuid", subID);
        return editor.commit();
    }

    public AuthState getAuthState() {
        return authState;
    }
}
