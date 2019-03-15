package com.philips.platform.pim.manager;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.platform.pim.fragment.PimFragment;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class PimAuthManager {
    private Fragment mFragment;



     void fetchAuthWellKnownConfiguration(String baseUrl) {
        // String discoveryEndpoint = configuration.kIssuer + "/.well-known/openid-configuration";
        String discoveryEndpoint = baseUrl + "/.well-known/openid-configuration";
//

        final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                (AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationException e) -> {
                    if (e != null) {
                        Log.d("", "Failed to retrieve configuration for " + e);
                    } else {
                        Log.d("", "Configuration retrieved for  proceeding"+ authorizationServiceConfiguration.discoveryDoc);
                        // makeAuthRequest(authorizationServiceConfiguration, mAuthService);
                    }
                };
        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(discoveryEndpoint), retrieveCallback);
    }


    void performLoginWithAccessToken(PimOidcDiscoveryManager pimOidcDiscoveryManager) {
       // makeAuthRequest(pimOidcDiscoveryManager.getAuthorizationServiceConfiguration(), mAuthService);
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
//        Log.d("PimAuthManager", "Making auth request to " + authorizationServiceConfiguration.authorizationEndpoint);
//        mAuthService.performAuthorizationRequest(
//                authorizationRequest, ((PimFragment) mFragment).createPostAuthorizationIntent(
//                        authorizationRequest,
//                        authorizationServiceConfiguration.discoveryDoc
//                ));
//
//    }


}
