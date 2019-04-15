package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;
import com.philips.platform.pim.utilities.PIMConstants;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
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

    protected void performTokenRequest(Context context, AuthorizationResponse authResponse, AuthorizationService.TokenResponseCallback tokenResponseCallback) {
        AuthorizationService authService = new AuthorizationService(context);
        TokenRequest tokenRequest = authResponse.createTokenExchangeRequest();
        authService.performTokenRequest(tokenRequest, tokenResponseCallback);
    }

    public Intent makeAuthRequest(Context pimFragmentContext, PIMOIDCConfigration pimoidcConfigration, Bundle mBundle) {
        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        pimoidcConfigration.getAuthorizationServiceConfiguration(), // the authorization service configuration
                        pimoidcConfigration.getClientId(), // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        Uri.parse(pimFragmentContext.getString(R.string.redirectURL))); // the redirect URI to which the auth response is sent
        AuthorizationRequest authRequest = authRequestBuilder
                .setScope(getScopes(mBundle))
                .build();
        AuthorizationService authService = new AuthorizationService(pimFragmentContext);
        return authService.getAuthorizationRequestIntent(authRequest);
    }

    private String getScopes(Bundle mBundle) {
        ArrayList<String> scopes = mBundle.getStringArrayList(PIMConstants.PIM_KEY_SCOPES);
        //Remove duplicate scopes if proposition add duplicates
        Set set = new HashSet();
        set.addAll(scopes);
        scopes.clear();
        scopes.addAll(set);

        StringBuilder stringBuilder = new StringBuilder();
        for (String scope : scopes) {
            stringBuilder = stringBuilder.append(scope + " ");
        }
        return stringBuilder.toString();
    }
}
