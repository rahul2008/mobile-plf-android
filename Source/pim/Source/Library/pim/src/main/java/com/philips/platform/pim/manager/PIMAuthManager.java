package com.philips.platform.pim.manager;

import android.net.Uri;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;
import com.philips.platform.pim.listeners.PIMOIDCAuthStateListener;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationServiceConfiguration;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;

    public PIMAuthManager(){
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    void fetchAuthWellKnownConfiguration(String baseUrl, PIMAuthorizationServiceConfigurationListener listener) {
        String discoveryEndpoint = baseUrl + "/.well-known/openid-configuration";
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration discoveryEndpoint : "+discoveryEndpoint);

        final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                (AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationException e) -> {
                    if (e != null) {
                        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : Failed to retrieve configuration for : "+e);
                        listener.onError();
                    } else {
                        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : Configuration retrieved for  proceeding : "+authorizationServiceConfiguration.discoveryDoc);
                        listener.onSuccess(authorizationServiceConfiguration.discoveryDoc);
                    }
                };
        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(discoveryEndpoint), retrieveCallback);
    }

    //for Login
    void performLoginWithAccessToken() {
        // makeAuthRequest(pimOidcDiscoveryManager.getAuthorizationServiceConfiguration(), mAuthService);
    }

    public void loginToOIDC(PIMOIDCConfigration pimoidcConfigration, PIMOIDCAuthStateListener pimoidcAuthStateListener) {
        //Get Authsate after authrization for AppAuth
    }



//    private void makeAuthRequest(@NonNull AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationService mAuthService) {
//
////        String customClaims = configuration.customClaims;
////        HashMap<String, String> map = new HashMap<>();
////        map.put("cookie_consent", "" + cookie_consent);
////        map.put("adobe_mc", adobeMc);
////        map.put("claims", customClaims);
////        map.put("ui_locales", uiLocales);
//        AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
//                authorizationServiceConfiguration,
//                "9317be6b-193f-4187-9ec2-5e1802a8d8ad",
//                ResponseTypeValues.CODE,
//                Uri.parse("com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect")).setScope("openid profile email address phone").build();
//        mAuthService.performAuthorizationRequest(
//                authorizationRequest, ((PimFragment) mFragment).createPostAuthorizationIntent(
//                        authorizationRequest,
//                        authorizationServiceConfiguration.discoveryDoc
//                ));
//
//    }


}
