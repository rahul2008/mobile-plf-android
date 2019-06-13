package com.philips.platform.pim.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.rest.AuthorizeRequest;
import com.philips.platform.pim.rest.IDAssertionRequest;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.utilities.PIMScopes;
import com.philips.platform.pim.utilities.UserCustomClaims;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.ResponseTypeValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.net.Uri.*;

public class PIMMigrationManager {
    private String TAG = PIMMigrationManager.class.getSimpleName();
    private Context mContext;

    public PIMMigrationManager(Context mContext) {
        this.mContext = mContext;
    }

    public String fetchOldUserData(AppInfraInterface appInfraInterface) {
        String response = appInfraInterface.getSecureStorage().fetchValueForKey("jr_capture_signed_in_user", new SecureStorageInterface.SecureStorageError());
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String accessToken = jsonObject.getString("accessToken");
                Log.i(TAG, "accessToken : " + accessToken);
                return accessToken;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void assertLegacyIDToken(String legacyToken) {
        IDAssertionRequest idAssertionRequest = new IDAssertionRequest("https://stg.api.eu-west-1.philips.com//consumerIdentityService/identityAssertions/", legacyToken);
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        pimRestClient.invokeRequest(idAssertionRequest, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String dataString = jsonObject.getString("data");
                String idassertion = (new JSONObject(dataString)).getString("identityAssertion");
                Log.i(TAG, "idasertion : " + idassertion);
               // performAuthRequest(idassertion);
                new RequestTask(idassertion).execute();
               /* AuthorizeRequest authrequest = new AuthorizeRequest(idassertion);
                pimRestClient.invokeRequest(authrequest, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse : " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse : " + error.networkResponse.statusCode);
                    }
                });*/

                //ew RequestTask(idassertion).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i(TAG, "error");
        });
        //new RequestTask(legacyToken).execute();
    }

    private void performAuthRequest(String id_token_hint) {
        String authorizeEndpoint = "https://stg.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login/authorize";
        RequestQueue requestQueue =  Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,authorizeEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse : " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-type", "application/x-www-form-urlencoded");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_token_hint", id_token_hint);
                params.put("redirect_uri", "com.philips.apps.94e28300-565d-4110-8919-42dc4f817393://oauthredirect");
                params.put("scope", "openid email profile phone");
                params.put("client_id", "94e28300-565d-4110-8919-42dc4f817393");
                params.put("response_type", "code");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    class RequestTask extends AsyncTask<Void, Void, Void> {
        private String legacyToken;

        RequestTask(String legacyToken) {
            this.legacyToken = legacyToken;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                performRequest(legacyToken);
                //performHTTPUrlRequest(legacyToken);
            } catch (Exception e) {
                Log.e(TAG, "Failed to establish connection.", e.fillInStackTrace());
            }
            return null;
        }
    }

    public void performRequest(String idTokenHint) {
        InputStream userInfoResponse = null;
        try {
            String url = "https://stg.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login/authorize";
            URL urlEndPoint = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) urlEndPoint.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setRequestProperty("Accept", "application/json");

            conn.connect();

            JSONObject bodyJson = new JSONObject();
            try {
                bodyJson.put("id_token_hint", idTokenHint);
                bodyJson.put("redirect_uri", "com.philips.apps.94e28300-565d-4110-8919-42dc4f817393://oauthredirect");
                bodyJson.put("scope", "openid email profile phone");
                bodyJson.put("client_id", "94e28300-565d-4110-8919-42dc4f817393");
                bodyJson.put("response_type", "code");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            conn.getOutputStream().write(bodyJson.toString().getBytes());
            /*OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(bodyJson.toString());
            wr.flush();*/
            int rescode = conn.getResponseCode();
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 204) {
                Log.d(TAG, "Previous access token is considered invalid");
            } else {
                Log.e(TAG, "Unable to revoke access token");
            }
            userInfoResponse = conn.getInputStream();
            String response = readStream(userInfoResponse);
            Log.i(TAG, "response : " + response);
        } catch (IOException ioEx) {
            Log.e(TAG, ioEx.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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

    private static String readStream(InputStream stream) throws IOException {
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

    private String getCustomClaims() {
        JsonObject customClaimObject = new JsonObject();
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_CONSENT, null);
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP, null);
        customClaimObject.add(UserCustomClaims.SOCIAL_PROFILES, null);
        customClaimObject.add(UserCustomClaims.UUID, null);

        JsonObject userInfo = new JsonObject();
        userInfo.add("userinfo", customClaimObject);
        PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "PIM_KEY_CUSTOM_CLAIMS: " + userInfo.toString());
        return userInfo.toString();
    }

}
