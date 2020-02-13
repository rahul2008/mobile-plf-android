package com.philips.it.uiddemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.Toast;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.adobe.mobile.MobilePrivacyStatus;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.ResponseTypeValues;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_AUTH = 100;
    private AuthStateManager mStateManager;
    private ExecutorService mExecutor;
    private ProgressBarButton loginButton;

    private Spinner localeSpinner;

    private CheckBox analyticsOptinCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStateManager = AuthStateManager.getInstance(this);
        mExecutor = Executors.newSingleThreadExecutor();
        analyticsOptinCheckBox = findViewById(R.id.analytics_checkBox);
        analyticsOptinCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);
            } else {
                Config.setPrivacyStatus(MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);
            }

        });
        if (mStateManager.getCurrent().isAuthorized()) {
            launchTokenActivity();
            return;
        }
        loginButton = findViewById(R.id.login_button);
        localeSpinner = findViewById(R.id.locale_spinner);
        loginButton.setOnClickListener(view -> {
            loginButton.showProgressIndicator();
            Map<String, Object> contextData = new HashMap<>();
            contextData.put("previousPageName", "digitalcare:contactus");
            Analytics.trackState("digitalcare:home", contextData);
            startAuthorizationFlow();
        });
    }

    private void launchTokenActivity() {
        startActivity(new Intent(this, TokenActivity.class));
        finish();
        return;
    }

    private void startAuthorizationFlow() {
        AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse("https://v2.api.us.janrain.com/c2a48310-9715-3beb-895e-000000000000/login"), new AuthorizationServiceConfiguration.RetrieveConfigurationCallback() {
            @Override
            public void onFetchConfigurationCompleted(@Nullable AuthorizationServiceConfiguration serviceConfiguration, @Nullable AuthorizationException ex) {
                if (serviceConfiguration == null) {
                    loginButton.hideProgressIndicator();
                    Toast.makeText(LoginActivity.this, "Error while fetching configuration!", Toast.LENGTH_LONG).show();
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("cookie_consent", "" + analyticsOptinCheckBox.isChecked());
                map.put("app_rep", "philipsmobileappsjanraindev");
                if (analyticsOptinCheckBox.isChecked()) {
                    map.put("adobe_mc", Analytics.getTrackingIdentifier());
                }

                map.put("ui_locales", localeSpinner.getSelectedItem().toString());
                AuthorizationRequest.Builder authRequestBuilder =
                        new AuthorizationRequest.Builder(
                                serviceConfiguration, // the authorization service configuration
                                "9317be6b-193f-4187-9ec2-5e1802a8d8ad", // the client ID, typically pre-registered and static
                                ResponseTypeValues.CODE, // the response_type value: we want a code
                                Uri.parse("com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect")); // the redirect URI to which the auth response is sent
                AuthorizationRequest authRequest = authRequestBuilder
                        .setScope("openid email profile https://idp.example.com/custom-scope")
                        .setLoginHint("jdoe@user.example.com").setAdditionalParameters(map)
                        .build();
                AuthorizationService authService = new AuthorizationService(LoginActivity.this);
                Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);
                startActivityForResult(authIntent, RC_AUTH);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED) {
            loginButton.hideProgressIndicator();
        } else {
            loginButton.setProgressText("Signing in...");
            AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);
            mStateManager.updateAfterAuthorization(response, ex);
            ((UIDDemoApplication) getApplication()).getAuthState().update(response, ex);
            final AuthorizationService authService = new AuthorizationService(LoginActivity.this);
            try {
                authService.performTokenRequest(
                        response.createTokenExchangeRequest(), ((UIDDemoApplication) getApplication()).authState.getClientAuthentication(),
                        (resp, ex1) -> {
                            loginButton.hideProgressIndicator();
                            mStateManager.updateAfterTokenResponse(resp, ex1);
                            ((UIDDemoApplication) getApplication()).getAuthState().update(resp, ex1);
                            if (resp != null) {
                                launchTokenActivity();
                                // exchange succeeded
                            } else {
                                Toast.makeText(LoginActivity.this, "Error while retrieving token!", Toast.LENGTH_LONG).show();
                                // authorization failed, check ex for more details
                            }
                        });
            } catch (ClientAuthentication.UnsupportedAuthenticationMethod unsupportedAuthenticationMethod) {
                unsupportedAuthenticationMethod.printStackTrace();
            }

        }

    }
}
