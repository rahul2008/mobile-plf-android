package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.models.PIMOIDCUserProfile;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.UserProfileRequest;

import net.openid.appauth.AuthState;

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
        //TODO : fetch authstate from secure storage
        pimoidcUserProfile = new PIMOIDCUserProfile(appInfraInterface.getSecureStorage(), authState);
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


    private void saveUserProfileToSecureStorage(String profileMap) {
       appInfraInterface.getSecureStorage().storeValueForKey("USER_PROFILE", profileMap.toString(), new SecureStorageInterface.SecureStorageError());
    }

    private void saveAuthStateIntoSecureStorage(AuthState authState) {
        appInfraInterface.getSecureStorage().storeValueForKey("AuthState", authState.jsonSerializeString(), new SecureStorageInterface.SecureStorageError());
    }

   /* public void saveUserProfileJsonToStorage(Context context){
        try {
            InputStream ins = context.getAssets().open("userprofile.json");
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();
            String jsonString = new String(buffer, "UTF-8");
            SecureStorage.SecureStorageError sserror = new SecureStorageInterface.SecureStorageError();
            secureStorage.storeValueForKey("uuid_userprofile",jsonString,sserror);
            mLoggingInterface.log(DEBUG,TAG,"Store uuid_userprofile errorcode: "+sserror.getErrorCode()+" errormessage : "+sserror.getErrorMessage());
        }catch (Exception ex){
        }
    }*/

}
