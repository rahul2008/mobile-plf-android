package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;
import com.philips.platform.pim.listeners.PIMOIDCAuthStateListener;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.util.HashMap;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private AuthState mAuthState;
    private AuthorizationService mAuthService;

    public PIMAuthManager(){
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    void fetchAuthWellKnownConfiguration(String baseUrl, PIMAuthorizationServiceConfigurationListener listener) {
        String discoveryEndpoint = baseUrl + "/.well-known/openid-configuration";
        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration discoveryEndpoint : "+discoveryEndpoint);

        final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                (AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationException e) -> {
                    if (e != null) {
                        mLoggingInterface.log(DEBUG,TAG,"fetchAuthWellKnownConfiguration : Failed to retrieve configuration for : "+e.getMessage());
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

    public void loginToOIDC(Context context, PIMOIDCConfigration pimoidcConfigration, PIMOIDCAuthStateListener pimoidcAuthStateListener) {
        //Get Authsate after authrization for AppAuth
        mAuthService = new AuthorizationService(context);
        Intent intent = (((FragmentActivity)context).getIntent());
        if (intent != null) {
            AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
            AuthorizationException exception = AuthorizationException.fromIntent(intent);

            // Check for creation, if not - create
            if (response != null || exception != null) {
                mAuthState = new AuthState(response, exception);
            }
            if (response != null) {
                //Log.d(TAG, "Received AuthorizationResponse");
                exchangeAuthorizationCode(response);
            } else {
                //Log.d(TAG, "Authorization failed: " + exception);
            }
        }
    }



    private void makeAuthRequest(@NonNull AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationService mAuthService) {

//        String customClaims = configuration.customClaims;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("cookie_consent", "" + cookie_consent);
//        map.put("adobe_mc", adobeMc);
//        map.put("claims", customClaims);
//        map.put("ui_locales", uiLocales);
        AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
                authorizationServiceConfiguration,
                "9317be6b-193f-4187-9ec2-5e1802a8d8ad",
                ResponseTypeValues.CODE,
                Uri.parse("com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect")).setScope("openid profile email address phone").build();
        mAuthService.performAuthorizationRequest(
                authorizationRequest, .createPostAuthorizationIntent(
                        authorizationRequest,
                        authorizationServiceConfiguration.discoveryDoc
                ));

    }


}
