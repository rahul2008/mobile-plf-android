package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.utilities.PIMScopes;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import java.util.ArrayList;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private AuthState mAuthState;

    PIMAuthManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    void fetchAuthWellKnownConfiguration(String baseUrl, PIMAuthorizationServiceConfigurationListener listener) {
        baseUrl = "https://tst.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login";
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

    Intent getAuthorizationRequestIntent(Context context, AuthorizationServiceConfiguration authServiceConfiguration, String clientID, Map parameter) {
        if (context == null || authServiceConfiguration == null || clientID == null)
            return null;

        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        authServiceConfiguration,
                        clientID,
                        ResponseTypeValues.CODE,
                        Uri.parse(context.getString(R.string.redirectURL)));

        AuthorizationRequest authRequest = authRequestBuilder
                .setScope(getScopes())
                .setAdditionalParameters(parameter)
                .build();
        AuthorizationService authService = new AuthorizationService(context);
        return authService.getAuthorizationRequestIntent(authRequest);
    }

    void performTokenRequest(Context context, Intent dataIntent, PIMLoginListener pimLoginListener) {
        if (context == null || dataIntent == null) {
            if (pimLoginListener != null)
                pimLoginListener.onLoginFailed(0);
            mLoggingInterface.log(DEBUG, TAG, "Token request failed. context :" + context + " dataIntent :" + dataIntent);
            return;
        }
        AuthorizationResponse response = AuthorizationResponse.fromIntent(dataIntent);
        AuthorizationException exception = AuthorizationException.fromIntent(dataIntent);

        if (exception != null || response != null) {
            mAuthState = new AuthState(response, exception);
        }
        AuthorizationService authService = new AuthorizationService(context);

        TokenRequest tokenRequest = response.createTokenExchangeRequest();
        authService.performTokenRequest(tokenRequest, new AuthorizationService.TokenResponseCallback() {
            @Override
            public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {
                if (response != null) {
                    mAuthState.update(response, ex);
                    mLoggingInterface.log(DEBUG, TAG, "onTokenRequestCompleted => access token : " + response.accessToken);
                    pimLoginListener.onLoginSuccess();
                }

                if (ex != null) {
                    mLoggingInterface.log(DEBUG, TAG, "Token Request failed with error : " + ex.getMessage());
                    pimLoginListener.onLoginFailed(ex.code);
                }
            }
        });
    }

    private String getScopes() {
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add(PIMScopes.PHONE);
        scopes.add(PIMScopes.EMAIL);
        scopes.add(PIMScopes.PROFILE);
        scopes.add(PIMScopes.ADDRESS);
        scopes.add(PIMScopes.OPENID);
        StringBuilder stringBuilder = new StringBuilder();
        for (String scope : scopes) {
            stringBuilder = stringBuilder.append(scope + " ");
        }
        return stringBuilder.toString();
    }


    AuthState getAuthState() {
        return mAuthState;
    }
}
