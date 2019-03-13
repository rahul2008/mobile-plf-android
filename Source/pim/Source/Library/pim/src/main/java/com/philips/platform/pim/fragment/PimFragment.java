package com.philips.platform.pim.fragment;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.PimActivity;
import com.philips.platform.pim.R;
import com.philips.platform.pim.integration.PimInterface;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;

public class PimFragment extends Fragment implements View.OnClickListener {
    private static final String EXTRA_AUTH_SERVICE_DISCOVERY = "authServiceDiscovery";
    private static final String EXTRA_AUTH_STATE = "authState";
    AuthorizationService mAuthService;
    private AuthState mAuthState = null;
    private Context mContext;
    private JSONObject mUserInfoJson;
    public static Boolean revoked = false;
    private static final String KEY_USER_INFO = "userInfo";
    public static final String SCOPE = "openid profile email address phone";
    public String kClientID = "9317be6b-193f-4187-9ec2-5e1802a8d8ad";
    public String kRedirectURI = "com.philips.apps.9317be6b-193f-4187-9ec2-5e1802a8d8ad://oauthredirect";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.udi_fragment, container, false);
//        getAdobeDataFromMainActivity();
        ImageButton tokenIcon = (ImageButton) v.findViewById(R.id.tokenIcon);
        ImageButton refreshIcon = (ImageButton) v.findViewById(R.id.refreshIcon);
        ImageButton userInfoIcon = (ImageButton) v.findViewById(R.id.userInfoIcon);
        ImageButton revokeTokensIcon = (ImageButton) v.findViewById(R.id.revokeTokensIcon);
        tokenIcon.setOnClickListener(this);
        refreshIcon.setOnClickListener(this);
        userInfoIcon.setOnClickListener(this);
        revokeTokensIcon.setOnClickListener(this);

        init(savedInstanceState);

        return v;
    }

    private void init(Bundle savedInstanceState) {
        mAuthService = new AuthorizationService(mContext);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_USER_INFO)) {
                try {
                    Log.d(TAG, "Grabbing userInfo from savedInstance");
                    mUserInfoJson = new JSONObject(savedInstanceState.getString(KEY_USER_INFO));
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse saved user info JSON", e);
                }
            }
        }

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (mAuthState == null) {
                AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
                AuthorizationException exception = AuthorizationException.fromIntent(intent);

                // Check for creation, if not - create
                if (response != null || exception != null) {
//                    mAuthState = new AuthState(response, exception);
                    mAuthState = readAuthState();
                }
                if (response != null) {
                    Log.d(TAG, "Received AuthorizationResponse");
                    exchangeAuthorizationCode(response);
                } else {
                    Log.d(TAG, "Authorization failed: " + exception);
                }
            }
        }
    }

    /**
     * Performs Authorization code exchange
     */
    private void exchangeAuthorizationCode(AuthorizationResponse authorizationResponse) {
        performTokenRequest(authorizationResponse.createTokenExchangeRequest());
    }

    /**
     * Sends request for Token
     */
    private void performTokenRequest(TokenRequest request) {
        mAuthService.performTokenRequest(
                request,
                new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(
                            @Nullable TokenResponse tokenResponse,
                            @Nullable AuthorizationException ex) {
                        if (tokenResponse != null)
                            receivedTokenResponse(tokenResponse, ex);
                        else
                            createAlert("Token", "Response : " + ex.getMessage());
                    }
                });
    }

    /**
     * Sets display text for token value
     */
    private void receivedTokenResponse(
            @Nullable TokenResponse tokenResponse,
            @Nullable AuthorizationException authException) {
        Log.d(TAG, "Token request complete");
        mAuthState.update(tokenResponse, authException);
        String msg = "Access Token: \n" + mAuthState.getAccessToken()
                + "\n\nID Token: \n" + mAuthState.getIdToken()
                + "\n\nRefresh Token: \n" + mAuthState.getRefreshToken();
        Log.d(TAG, msg);
        createAlert("Tokens", msg);
        writeAuthState(mAuthState);
    }

    /**
     * Creates UI Alert given Title and Message
     *
     * @param title:   Title of alert
     * @param message: Message body of alert
     */
    public void createAlert(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(true);

        alert.setNegativeButton(
                "OK",
                (dialog, id) -> dialog.cancel());

        AlertDialog dialog = alert.create();
        dialog.show();
    }


    /**
     * Refreshes access and id token with refresh token
     */
    public void refreshTokens() {
        if (mAuthState != null) {
            performTokenRequest(mAuthState.createTokenRefreshRequest());
            writeAuthState(mAuthState);
            Log.d(TAG, "Refreshed Access Token");
        } else {
            createAlert("Error", "Not authenticated");
            Log.d(TAG, "Not authenticated");
        }
    }

    /**
     * Revokes current access token
     */
    @SuppressLint("CheckResult")
    public void revokeTokens() {
        if (mAuthState != null) {
            Log.d(TAG, "Revoking Tokens");

//            Observable.just(null)
//                    .map(this::callRevokeEndpoint)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::tokenRevoedSuccess);
            // Call revoke endpoint to terminate access_token
            AsyncTask revokeTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    callRevokeEndpoint();
                    return null;
                }

                @Override
                protected void onPostExecute(Object result) {
                    tokenRevoedSuccess();
                }

                ;
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                revokeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                revokeTask.execute();
            }

        } else {
            createAlert("Error", "Not authenticated");
            Log.d(TAG, "Not authenticated");
        }
    }

    private void tokenRevoedSuccess() {
        mAuthState.setNeedsTokenRefresh(true);
        createAlert("Success", "Tokens revoked");
        revoked = true;
        mAuthState = null;
    }

    public void callRevokeEndpoint() {
        try {
            if (mAuthState.getAuthorizationServiceConfiguration() == null) {
                Log.d(TAG, "Cannot make request without service configuration");
            }

            AuthorizationServiceDiscovery discoveryDoc = getDiscoveryDocFromIntent(getActivity().getIntent());
            if (discoveryDoc == null) {
                throw new IllegalStateException("no available discovery doc");
            }

            try {
                URL url = new URL(discoveryDoc.getIssuer() + "/token/revoke");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String params = "client_id=" + kClientID + "&token=" + mAuthState.getAccessToken();
                byte[] postParams = params.getBytes("UTF-8");
                conn.setRequestMethod("POST");
                conn.setInstanceFollowRedirects(false);
                conn.setDoOutput(true);
                conn.connect();
                conn.getOutputStream().write(postParams);
                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 204) {
                    Log.d(TAG, "Previous access token is considered invalid");
                    mAuthState.setNeedsTokenRefresh(true);

                } else {
                    Log.e(TAG, "Unable to revoke access token");
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to establish connection.", e.fillInStackTrace());
        }
    }

    /**
     * Gets endpoints from configured domain
     *
     * @param intent: Browser intent
     * @return
     */
    static AuthorizationServiceDiscovery getDiscoveryDocFromIntent(Intent intent) {
        if (!intent.hasExtra(EXTRA_AUTH_SERVICE_DISCOVERY)) {
            return null;
        }
        String discoveryJson = intent.getStringExtra(EXTRA_AUTH_SERVICE_DISCOVERY);
        try {
            return new AuthorizationServiceDiscovery(new JSONObject(discoveryJson));
        } catch (JSONException | AuthorizationServiceDiscovery.MissingArgumentException ex) {
            throw new IllegalStateException("Malformed JSON in discovery doc");
        }
    }

    /**
     * Sends authorization request to authorization endpoint
     */
    public void sendAuthorizationRequestFrag() {
        String issuer = "https://v2.api.us.janrain.com/c2a48310-9715-3beb-895e-000000000000/login";
        final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                (authorizationServiceConfiguration, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Failed to retrieve configuration for " + issuer, e);
                    } else {
                        Log.d(TAG, "Configuration retrieved for " + issuer + ", proceeding");
                        makeAuthRequest(authorizationServiceConfiguration);
                    }
                };
        String discoveryEndpoint = issuer + "/.well-known/openid-configuration";
        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(discoveryEndpoint), retrieveCallback);
    }

    /**
     * Makes authentication request to endpoints in discovery document
     *
     * @param authorizationServiceConfiguration: AppAuth authorizationService detail
     */
    private void makeAuthRequest(
            @NonNull AuthorizationServiceConfiguration authorizationServiceConfiguration
    ) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("cookie_consent", "" + cookie_consent);
//        map.put("adobe_mc", adobeMc);
//
//        map.put("ui_locales", uiLocales);
        AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
                authorizationServiceConfiguration,
                kClientID,
                ResponseTypeValues.CODE,
                Uri.parse(kRedirectURI)).setScope(SCOPE).build();
        Log.d(TAG, "Making auth request to " + authorizationServiceConfiguration.authorizationEndpoint);
        mAuthService.performAuthorizationRequest(
                authorizationRequest,
                createPostAuthorizationIntent(
                        mContext.getApplicationContext(),
                        authorizationRequest,
                        authorizationServiceConfiguration.discoveryDoc
                ));

    }


    /**
     * Starts webView intent
     */
    static PendingIntent createPostAuthorizationIntent(
            @NonNull Context context,
            @NonNull AuthorizationRequest request,
            @Nullable AuthorizationServiceDiscovery discoveryDoc
    ) {
        Intent intent = new Intent(context, PimActivity.class);
        intent.putExtra(EXTRA_AUTH_STATE, discoveryDoc.docJson.toString());
        if (discoveryDoc != null) {
            intent.putExtra(EXTRA_AUTH_SERVICE_DISCOVERY, discoveryDoc.docJson.toString());
        }

        return PendingIntent.getActivity(context, request.hashCode(), intent, 0);
    }


    /**
     * Starts async task for calling /userinfo endpoint from display
     */
    public void fetchUserInfo() {
        if (mAuthState != null) {
            PimFragment.RequestTask requestTask = new RequestTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                requestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                requestTask.execute();
            }
        } else {
            Log.d(TAG, "Not authenticated");
            createAlert("Error", "Not authenticated");
        }

    }

    /**
     * Get method for retrieving current authState
     *
     * @return mAuthState
     */
    public AuthState getAuthState() {
        return mAuthState;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tokenIcon) {
            sendAuthorizationRequestFrag();

        } else if (i == R.id.refreshIcon) {
            refreshTokens();

        } else if (i == R.id.userInfoIcon) {
            fetchUserInfo();

        } else if (i == R.id.revokeTokensIcon) {
            revokeTokens();

        }
    }

    /**
     * Calls userinfo endpoint through AsyncTask
     */
    class RequestTask extends AsyncTask<Void, Void, Void> {
        AuthState mAuthState = getAuthState();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String token = mAuthState.getAccessToken();
                if (!revoked) {
                    mAuthState.performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                        @Override
                        public void execute(String accessToken, String idToken, AuthorizationException ex) {
                            if (ex != null) {
                                Log.d(TAG, "Token refresh failed when fetching user info");
                                return;
                            }
                            performRequest(accessToken);
                        }
                    });
                } else {
                    performRequest(token);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to establish connection.", e.fillInStackTrace());
            }
            return null;
        }
    }

    /**
     * Performs HTTP Request with access token
     */
    public void performRequest(String token) {
        if (mAuthState.getAuthorizationServiceConfiguration() == null) {
            Log.d(TAG, "Cannot make userInfo request without service configuration");
        }

        AuthorizationServiceDiscovery discoveryDoc = getDiscoveryDocFromIntent(getActivity().getIntent());
        if (discoveryDoc == null) {
            throw new IllegalStateException("no available discovery doc");
        }

        URL userInfoEndpoint = null;

        try {
            userInfoEndpoint = new URL(discoveryDoc.getUserinfoEndpoint().toString());
        } catch (MalformedURLException urlEx) {
            Log.e(TAG, "Failed to construct user info endpoint URL", urlEx);
        }

        InputStream userInfoResponse = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) userInfoEndpoint.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setInstanceFollowRedirects(false);

            if (conn.getResponseCode() == 401) {
                Log.e(TAG, "Access Token Invalid");
                updateUserInfo(new JSONObject(("{'invalid_token' : 'The access token is invalid'}")));
            }

            userInfoResponse = conn.getInputStream();
            String response = readStream(userInfoResponse);
            updateUserInfo(new JSONObject(response));

        } catch (IOException ioEx) {
            Log.e(TAG, "Network error when querying userinfo endpoint", ioEx);
            System.out.println("Err: " + ioEx.getMessage());
        } catch (JSONException jsonEx) {
            Log.e(TAG, "Failed to parse userinfo response");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (userInfoResponse != null) {
                try {
                    userInfoResponse.close();
                } catch (IOException ioEx) {
                    Log.e(TAG, "Failed to close userinfo response stream", ioEx);
                }
            }
        }

    }

    /**
     * Updates User ID information on display
     *
     * @param jsonObject: Response JSON
     */
    private void updateUserInfo(final JSONObject jsonObject) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mUserInfoJson = jsonObject;
                Log.d(TAG, jsonObject.toString());
                createAlert("User Info", jsonObject.toString()
                        .replace("\\/", "/")
                        .replace(",", ",\n  ")
                        .replace("{", "{\n  ")
                        .replace("}", "}\n  "));
            }
        });
    }


    /**
     * Reads input into the buffer
     *
     * @param stream: HTTP response stream
     * @return String
     * @throws IOException
     */
    public static String readStream(InputStream stream) throws IOException {
        int BUFFER_SIZE = 1024;

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        char[] buffer = new char[BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();
        int readCount;
        while ((readCount = br.read(buffer)) != -1) {
            sb.append(buffer, 0, readCount);
        }
        return sb.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAuthService.dispose();
    }

//    @NonNull
//    public AuthState replace(@NonNull AuthState state) {
//        writeState(state);
////        mCurrentAuthState.set(state);
//        return state;
//    }


    @NonNull
    public AuthState readAuthState() {
//        SharedPreferences authPrefs = mContext.getSharedPreferences("auth", MODE_PRIVATE);
//        String stateJson = authPrefs.getString("stateJson", "");
        if (mAuthState != null) {
            try {
                String authData = getAuthFromSecureStorage(PimInterface.sAppInfraInterface.getSecureStorage());
                return AuthState.jsonDeserialize(authData);//fromJsonString(stateJson);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new AuthState();
        }
    }

    public void writeAuthState(@NonNull AuthState state) {
//        SharedPreferences authPrefs = mContext.getSharedPreferences("auth", MODE_PRIVATE);
//        authPrefs.edit()
//                .putString("stateJson", state.jsonSerializeString())
//                .apply();
//

        String data = state.jsonSerializeString();
        storeAuthInSecureStorage(PimInterface.sAppInfraInterface.getSecureStorage(), data);
        byte[] byteArray = new byte[0];
        try {
            byteArray = data.getBytes("UTF-8");
            long size = byteArray.length;
            System.out.println("Size of AuthState" + size);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void storeAuthInSecureStorage(SecureStorageInterface secureStorageInterface, String data) {
        secureStorageInterface.storeValueForKey("auth", data, new SecureStorageInterface.SecureStorageError());
    }

    private String getAuthFromSecureStorage(SecureStorageInterface secureStorageInterface) {
        return secureStorageInterface.fetchValueForKey("auth", new SecureStorageInterface.SecureStorageError());
    }


}
