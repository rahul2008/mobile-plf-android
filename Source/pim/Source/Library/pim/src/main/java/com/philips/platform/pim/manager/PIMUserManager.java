package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.models.PIMOIDCUserProfile;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.UserProfileRequest;

import net.openid.appauth.AuthState;

public class PIMUserManager {
    private PIMOIDCUserProfile pimoidcUserProfile;
    private SecureStorageInterface secureStorage;
    private AuthState authState;

    public void init(SecureStorageInterface secureStorage) {
        //get Secure Storage user profile
        this.secureStorage = secureStorage;

        //TODO : fetch authstate from secure storage
        pimoidcUserProfile = new PIMOIDCUserProfile(secureStorage, authState);
    }



    public void requestUserProfile(AuthState oidcAuthState, PIMListener pimListener) {
        //Create PimRequestInterface with confiration
        UserProfileRequest userProfileRequest = new UserProfileRequest(oidcAuthState.getLastAuthorizationResponse().request.configuration);
        new PIMRestClient(PIMSettingManager.getInstance().getRestClient()).invokeRequest(userProfileRequest, (String response) -> {
            onSuccess(response);
        }, error -> onError());
        //Store AuthState and fetch user profile then authstate

    }

    private void onError() {
        //Log
    }

    private void onSuccess(String resprofileMap) {
        //saveAuthStateIntoSecureStorage(apthState);
        saveUserProfileToSecureStorage(resprofileMap);
        //TODO: set inmemory oidc user profile
    }

    public PIMOIDCUserProfile getOIDCUserProfile() {
        return pimoidcUserProfile;
    }

//    public AuthState getAuthState() {
//        return
//    }


    private void saveUserProfileToSecureStorage(String profileMap) {
        //  pimoidcUserProfile = new PIMOIDCUserProfile(secureStorage, profileMap);
        secureStorage.storeValueForKey("USER_PROFILE", profileMap.toString(), new SecureStorageInterface.SecureStorageError());
    }

    private void saveAuthStateIntoSecureStorage(AuthState authState) {
        secureStorage.storeValueForKey("AuthState", authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
    }


}
