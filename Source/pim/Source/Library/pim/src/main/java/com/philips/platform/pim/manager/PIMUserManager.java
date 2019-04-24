package com.philips.platform.pim.manager;

import android.util.Log;

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
                String key = PIMSettingManager.getInstance().getKeyForStoringUserInfo(response);
                if(key != null)
                    saveUserProfileToSecureStorage(key,response);
                else
                    mLoggingInterface.log(DEBUG,TAG,"Key is null");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"error : "+error);
            }
        });
        //Store AuthState and fetch user profile then authstate
    }

    private void onError(String resprofileMap) {
        Log.d(TAG,"resprofileMap"+resprofileMap);
    }

    private void onSuccess(String resprofileMap) {
        //saveAuthStateIntoSecureStorage(apthState);

        //TODO: set inmemory oidc user profile
    }

    public PIMOIDCUserProfile getOIDCUserProfile() {
        return pimoidcUserProfile;
    }

    public AuthState getAuthState() {
        return authState;
    }

    private void saveUserProfileToSecureStorage(String key,String jsonUserProfile) {
        boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey(key, jsonUserProfile, new SecureStorageInterface.SecureStorageError());
        mLoggingInterface.log(DEBUG,TAG,"UserProfile Stored to Secure storage : "+isStored);
    }

    private void saveAuthStateIntoSecureStorage(AuthState authState) {
        boolean isStored = appInfraInterface.getSecureStorage().storeValueForKey("AuthState", authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
        mLoggingInterface.log(DEBUG,TAG,"AuthState Stored to Secure storage : "+isStored);
    }
}
