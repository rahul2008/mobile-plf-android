package com.philips.platform.pim.manager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.models.PIMOIDCUserProfile;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.UserProfileRequest;

import net.openid.appauth.AuthState;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMUserManager {
    private PIMOIDCUserProfile pimoidcUserProfile;
    private AuthState authState;
    private AppInfraInterface appInfraInterface;
    private LoggingInterface mLoggingInterface;
    private final String TAG = PIMUserManager.class.getSimpleName();

    public void init(AppInfraInterface appInfraInterface) {
        //get Secure Storage user profile
        this.appInfraInterface = appInfraInterface;
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        //TODO : Deepthi apr 15 fetch authstate from secure storage, auth state fetching is not clear.
        if (authState == null)
            pimoidcUserProfile = new PIMOIDCUserProfile(appInfraInterface.getSecureStorage(), authState);
    }


    public void requestUserProfile(AuthState oidcAuthState) {
        UserProfileRequest userProfileRequest = new UserProfileRequest(oidcAuthState);
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        pimRestClient.invokeRequest(userProfileRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveUserProfileAuthState(response, authState);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoggingInterface.log(DEBUG,TAG, "error : " + error.getMessage());
            }
        });
        //Store AuthState and fetch user profile then authstate
    }

    public PIMOIDCUserProfile getOIDCUserProfile() {
        return pimoidcUserProfile;
    }

    public AuthState getAuthState() {
        return authState;
    }

    private void saveUserProfileAuthState(String jsonUserProfileResponse, AuthState authState) {
        storeUserProfileToSecureStorage(jsonUserProfileResponse);

        String authStateKey = PIMSettingManager.getInstance().getKeyForStoringAuthState(jsonUserProfileResponse);
        storeAuthStateToSecureStorage(authStateKey, authState);
    }

    private void storeUserProfileToSecureStorage(String jsonUserProfileResponse) {
        String userInfoKey = PIMSettingManager.getInstance().getKeyForStoringUserInfo(jsonUserProfileResponse);
        if (userInfoKey != null) {
            boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(userInfoKey, jsonUserProfileResponse, new SecureStorageInterface.SecureStorageError());
            mLoggingInterface.log(DEBUG, TAG, "UserProfile Stored to Secure storage : " + isStored);
        } else
            mLoggingInterface.log(DEBUG, TAG, "Key is null");
    }

    private void storeAuthStateToSecureStorage(String authStateKey, AuthState authState) {
        if (authStateKey != null) {
            boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(authStateKey, authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
            mLoggingInterface.log(DEBUG, TAG, "AuthState Stored to Secure storage : " + isStored);
        } else
            mLoggingInterface.log(DEBUG, TAG, "authStateKey is null");

    }
}
