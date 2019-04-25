package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.utilities.PIMConstants;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMAuthManager {
    private final String TAG = PIMAuthManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private AuthState mAuthState;

    public PIMAuthManager() {
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    protected void fetchAuthWellKnownConfiguration(String baseUrl, PIMAuthorizationServiceConfigurationListener listener) {
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

    public Intent getAuthorizationRequestIntent(Context context,AuthorizationServiceConfiguration authServiceConfiguration,String clientID,Bundle bundle){
        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        authServiceConfiguration,
                        clientID,
                        ResponseTypeValues.CODE,
                        Uri.parse(context.getString(R.string.redirectURL)));
        Map<String, String> parameter = new HashMap<>();

        if(bundle != null) {
            Serializable serializable = bundle.getSerializable(PIMConstants.PIM_KEY_CUSTOM_CLAIMS);
            String customClaims = "id_token =" + serializable.toString();
            parameter.put("claims", customClaims);
        }else {
            mLoggingInterface.log(DEBUG,TAG,"Custom clain not set. Reason : bundle is null");
        }
        AuthorizationRequest authRequest = authRequestBuilder
                .setScope(getScopes(bundle))
//                .setAdditionalParameters(parameter)
                .build();
        AuthorizationService authService = new AuthorizationService(context);
        Intent authReqIntent = authService.getAuthorizationRequestIntent(authRequest);
        return authReqIntent;
    }

    protected void performTokenRequest(Context context, Intent dataIntent, PIMLoginListener pimLoginListener) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(dataIntent);
        AuthorizationException exception = AuthorizationException.fromIntent(dataIntent);

        if (exception != null || response != null) {
            mAuthState = new AuthState(response, exception);
        }
        mLoggingInterface.log(DEBUG, TAG, "performTokenRequest for code exchange to get Access token");
        AuthorizationService authService = new AuthorizationService(context);

        TokenRequest tokenRequest = response.createTokenExchangeRequest();
        authService.performTokenRequest(tokenRequest, new AuthorizationService.TokenResponseCallback() {
            @Override
            public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {
                if (response != null) {
                    mAuthState.update(response,ex);
                    mLoggingInterface.log(DEBUG, TAG, "onTokenRequestCompleted => access token : "+response.accessToken);
                    pimLoginListener.onLoginSuccess();
                }

                if(ex != null) {
                    mLoggingInterface.log(DEBUG, TAG, "Token Request failed with error : " + ex.getMessage());
                    pimLoginListener.onLoginFailed(ex.code);
                }
            }
        });
    }

    private String getScopes(Bundle mBundle) {
        ArrayList<String> scopes = mBundle.getStringArrayList(PIMConstants.PIM_KEY_SCOPES);
        StringBuilder stringBuilder = new StringBuilder();
        for (String scope : scopes) {
            stringBuilder = stringBuilder.append(scope + " ");
        }
        return stringBuilder.toString();
    }

    public AuthState getAuthState() {
        return mAuthState;
    }
}
