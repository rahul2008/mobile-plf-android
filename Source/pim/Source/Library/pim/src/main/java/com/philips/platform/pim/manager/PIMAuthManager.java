package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private static final String EXTRA_AUTH_STATE = "authState";
    private static final String EXTRA_AUTH_SERVICE_DISCOVERY = "authServiceDiscovery";
    private ExecutorService executorService;

    public PIMAuthManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        executorService = Executors.newSingleThreadExecutor();
    }

    void fetchAuthWellKnownConfiguration(String baseUrl, PIMAuthorizationServiceConfigurationListener listener) {
        String discoveryEndpoint = baseUrl + "/.well-known/openid-configuration";
        mLoggingInterface.log(DEBUG, TAG, "fetchAuthWellKnownConfiguration discoveryEndpoint : " + discoveryEndpoint);

        final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                (AuthorizationServiceConfiguration authorizationServiceConfiguration, AuthorizationException e) -> {
                    if (e != null) {
                        mLoggingInterface.log(DEBUG, TAG, "fetchAuthWellKnownConfiguration : Failed to retrieve configuration for : " + e.getMessage());
                        listener.onError(e.getMessage());
                    } else {
                        mLoggingInterface.log(DEBUG, TAG, "fetchAuthWellKnownConfiguration : Configuration retrieved for  proceeding : " + authorizationServiceConfiguration);
                        listener.onSuccess(authorizationServiceConfiguration);
                    }
                };
        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(discoveryEndpoint), retrieveCallback);
    }

//    protected void makeAuthRequest(PIMFragment pimFragment) {
//        AuthorizationServiceConfiguration serviceConfiguration = null;
//        try {
//            serviceConfiguration = PIMSettingManager.getInstance().getPimOidcConfigration().getAuthorizationServiceConfiguration();
//        } catch (NullPointerException exp) {
//            mLoggingInterface.log(DEBUG, TAG, "Service configuration is not available");
//        }
//
//        AuthorizationRequest.Builder authRequestBuilder =
//                new AuthorizationRequest.Builder(
//                        serviceConfiguration, // the authorization service configuration
//                        "9317be6b-193f-4187-9ec2-5e1802a8d8ad", // the client ID, typically pre-registered and static
//                        ResponseTypeValues.CODE, // the response_type value: we want a code
//                        Uri.parse("com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect")); // the redirect URI to which the auth response is sent
//        AuthorizationRequest authRequest = authRequestBuilder
//                .setScope("openid email profile")
//                .build();
//        AuthorizationService authService = new AuthorizationService(pimFragment.getContext());
//        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);
//
//        pimFragment.startActivityForResult(authIntent, 100);
//    }

    protected void performTokenRequest(Context context, AuthorizationResponse authResponse, AuthorizationService.TokenResponseCallback tokenResponseCallback) {
        AuthorizationService authService = new AuthorizationService(context);
        TokenRequest tokenRequest = authResponse.createTokenExchangeRequest();
        authService.performTokenRequest(tokenRequest, tokenResponseCallback);
    }

    public Intent makeAuthRequest(Context pimFragmentContext, PIMOIDCConfigration pimoidcConfigration) {
        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        pimoidcConfigration.getAuthorizationServiceConfiguration(), // the authorization service configuration
                        pimoidcConfigration.getClientId(), // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        Uri.parse("com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect")); // the redirect URI to which the auth response is sent
        AuthorizationRequest authRequest = authRequestBuilder
                .setScope("openid email profile")
                .build();
        AuthorizationService authService = new AuthorizationService(pimFragmentContext);
        return authService.getAuthorizationRequestIntent(authRequest);
    }
}
