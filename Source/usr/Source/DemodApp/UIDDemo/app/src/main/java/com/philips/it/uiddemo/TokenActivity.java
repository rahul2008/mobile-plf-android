package com.philips.it.uiddemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.MainThread;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.uid.view.widget.ProgressBarButton;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import okio.Okio;

public class TokenActivity extends AppCompatActivity {

    private ProgressBarButton logoutButton;

    private TextView userInfoTextView;

    private ExecutorService mExecutor;

    private AuthStateManager mStateManager;

    private final AtomicReference<JSONObject> mUserInfoJson = new AtomicReference<>();

    private static final String KEY_USER_INFO = "userInfo";
    private AuthorizationService mAuthService;
    private ProgressBarButton userInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        mStateManager=AuthStateManager.getInstance(this);
        mExecutor= Executors.newSingleThreadExecutor();
        logoutButton=findViewById(R.id.logout_button);
        userInfoTextView=findViewById(R.id.user_info_textview);
        userInfoButton =findViewById(R.id.user_info_button);
        userInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoButton.showProgressIndicator();
                mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, TokenActivity.this::fetchUserInfo);
            }
        });
        mAuthService=new AuthorizationService(this);
        logoutButton.setOnClickListener(view -> {
            logoutButton.showProgressIndicator();
            signOut();
        });

        if (savedInstanceState != null) {
            try {
                mUserInfoJson.set(new JSONObject(savedInstanceState.getString(KEY_USER_INFO)));
            } catch (JSONException ex) {
                Log.e("test", "Failed to parse saved user info JSON, discarding", ex);
            }
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // user info is retained to survive activity restarts, such as when rotating the
        // device or switching apps. This isn't essential, but it helps provide a less
        // jarring UX when these events occur - data does not just disappear from the view.
        if (mUserInfoJson.get() != null) {
            state.putString(KEY_USER_INFO, mUserInfoJson.toString());
        }
    }

    private void signOut() {
        // discard the authorization and token state, but retain the configuration and
        // dynamic client registration (if applicable), to save from retrieving them again.
        AuthState currentState = AuthStateManager.getInstance(this).getCurrent();
        AuthState clearedState =
                new AuthState(currentState.getAuthorizationServiceConfiguration());
        if (currentState.getLastRegistrationResponse() != null) {
            clearedState.update(currentState.getLastRegistrationResponse());
        }
        AuthStateManager.getInstance(this).replace(clearedState);

        Intent mainIntent = new Intent(this, LoginActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        logoutButton.hideProgressIndicator();
        finish();
    }

    private void fetchUserInfo(String accessToken, String idToken, AuthorizationException ex) {
        if (ex != null) {
            Log.e("test", "Token refresh failed when fetching user info");
            mUserInfoJson.set(null);
            return;
        }

        AuthorizationServiceDiscovery discovery =
                mStateManager.getCurrent()
                        .getAuthorizationServiceConfiguration()
                        .discoveryDoc;


        try {
            URL userInfoEndpoint= new URL(discovery.getUserinfoEndpoint().toString());
            mExecutor.submit(() -> {
                try {
                    HttpURLConnection conn =
                            (HttpURLConnection) userInfoEndpoint.openConnection();
                    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                    conn.setInstanceFollowRedirects(false);
                    String response = Okio.buffer(Okio.source(conn.getInputStream()))
                            .readString(Charset.forName("UTF-8"));
                    mUserInfoJson.set(new JSONObject(response));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userInfoButton.hideProgressIndicator();
                            userInfoTextView.setText(mUserInfoJson.toString());
                        }
                    });


                } catch (IOException ioEx) {
                    Log.e("test", "Network error when querying userinfo endpoint", ioEx);
                    showSnackbar("Fetching user info failed");
                } catch (JSONException jsonEx) {
                    Log.e("test", "Failed to parse userinfo response");
                    showSnackbar("Failed to parse user info");
                }

            });
        } catch (MalformedURLException e) {
            Log.e("test", "Failed to construct user info endpoint URL");
        }


    }

    @MainThread
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinator),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }
}
