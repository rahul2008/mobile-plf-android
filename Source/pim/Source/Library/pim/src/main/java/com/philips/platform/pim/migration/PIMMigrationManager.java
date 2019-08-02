package com.philips.platform.pim.migration;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.errors.PIMErrorEnums;
import com.philips.platform.pim.listeners.PIMUserMigrationListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.rest.IDAssertionRequest;
import com.philips.platform.pim.rest.PIMMigrationAuthRequest;
import com.philips.platform.pim.rest.PIMRequestInterface;
import com.philips.platform.pim.rest.PIMRestClient;

import net.openid.appauth.AuthorizationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

class PIMMigrationManager {

    //TODO: Shashi, This is temporary, Need to fetch from OIDC configuration later.
    private String ID_ASSERTION_ENDPOINT = "https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/";
    private final String TAG = PIMMigrationManager.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private Context mContext;
    private PIMLoginManager pimLoginManager;
    private AuthorizationRequest authorizationRequest;
    private PIMUserMigrationListener pimUserMigrationListener;

    public PIMMigrationManager(Context context, PIMUserMigrationListener pimUserMigrationListener) {
        mContext = context;
        this.pimUserMigrationListener = pimUserMigrationListener;
        pimLoginManager = new PIMLoginManager(mContext, PIMSettingManager.getInstance().getPimOidcConfigration());
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
    }

    void migrateUser(String usrAccessToken) {
        performIDAssertion(usrAccessToken);
    }

    private void performIDAssertion(String usrAccessToken) {
        IDAssertionRequest idAssertionRequest = new IDAssertionRequest(ID_ASSERTION_ENDPOINT, usrAccessToken);
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        pimRestClient.invokeRequest(idAssertionRequest, getSuccessListener(idAssertionRequest), getErrorListener(idAssertionRequest));
    }

    @VisibleForTesting
    void performAuthorization(String id_token_hint) {
        authorizationRequest = pimLoginManager.createAuthRequestUriForMigration(createAdditionalParameterForMigration(id_token_hint));
        if (authorizationRequest == null) {
            mLoggingInterface.log(DEBUG, TAG, "performAuthorization failed. Cause : authorizationRequest is null.");
            pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext,PIMErrorEnums.MIGRATION_FAILED.errorCode)));
            return;
        }

        PIMMigrationAuthRequest pimMigrationAuthRequest = new PIMMigrationAuthRequest(authorizationRequest.toUri().toString());
        PIMRestClient pimRestClient = new PIMRestClient(PIMSettingManager.getInstance().getRestClient());
        HttpsURLConnection.setFollowRedirects(false);
        pimRestClient.invokeRequest(pimMigrationAuthRequest, getSuccessListener(pimMigrationAuthRequest), getErrorListener(pimMigrationAuthRequest));
    }

    @VisibleForTesting
    Response.Listener getSuccessListener(PIMRequestInterface reqType) {
        return (Response.Listener<String>) response -> {
            if (response == null) {
                pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext, PIMErrorEnums.MIGRATION_FAILED.errorCode)));
                return;
            }

            if (reqType instanceof IDAssertionRequest) {
                String id_token_hint = parseIDAssertionFromJSONResponse(response);
                mLoggingInterface.log(DEBUG, TAG, "ID Assertion request success. ID_token_hint : " + id_token_hint);
                performAuthorization(id_token_hint);
            } else if (reqType instanceof PIMMigrationAuthRequest) {
                mLoggingInterface.log(DEBUG, TAG, "Token auth request failed."); //PIMMigrationAuthRequest response comes with 302 code and volley throw 302 response code in error.So,handling in error listener
                pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext, PIMErrorEnums.MIGRATION_FAILED.errorCode)));
            }
        };
    }

    @VisibleForTesting
    Response.ErrorListener getErrorListener(PIMRequestInterface reqType) {
        return error -> {
            if (error == null) {
                return;
            }

            if (reqType instanceof IDAssertionRequest) {
                mLoggingInterface.log(DEBUG, TAG, "Failed in ID Assertion Request. Error : " + error.getMessage());
                pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext,PIMErrorEnums.MIGRATION_FAILED.errorCode)));
            } else if (reqType instanceof PIMMigrationAuthRequest) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 302) {
                    String authRsponse = networkResponse.headers.get("Location");
                    mLoggingInterface.log(DEBUG, TAG, "Authorization response success : " + authRsponse);
                    pimLoginManager.exchangeAuthorizationCodeForMigration(authorizationRequest, authRsponse, pimUserMigrationListener);
                } else {
                    mLoggingInterface.log(DEBUG, TAG, "Token auth request failed.");
                    pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext,PIMErrorEnums.MIGRATION_FAILED.errorCode)));
                }
            }
        };
    }

    @VisibleForTesting
    Map<String, String> createAdditionalParameterForMigration(String id_token_hint) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("id_token_hint", id_token_hint);
        parameter.put("claims", PIMSettingManager.getInstance().getPimOidcConfigration().getCustomClaims());
        return parameter;
    }

    @VisibleForTesting
    String parseIDAssertionFromJSONResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            String id_token_hint = dataObject.getString("identityAssertion");
            return id_token_hint;
        } catch (JSONException e) {
            mLoggingInterface.log(DEBUG, TAG, "parseIDAssertionFromJSONResponse failed. Error : " + e.getMessage());
            pimUserMigrationListener.onUserMigrationFailed(new Error(PIMErrorEnums.MIGRATION_FAILED.errorCode, PIMErrorEnums.MIGRATION_FAILED.getLocalisedErrorDesc(mContext,PIMErrorEnums.MIGRATION_FAILED.errorCode)));
        }
        return null;
    }
}
