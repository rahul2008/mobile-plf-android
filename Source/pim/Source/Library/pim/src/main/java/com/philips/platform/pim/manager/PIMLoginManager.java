package com.philips.platform.pim.manager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Visitor;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.listeners.PIMTokenRequestListener;
import com.philips.platform.pim.listeners.PIMUserMigrationListener;
import com.philips.platform.pim.listeners.PIMUserProfileDownloadListener;
import com.philips.platform.pim.utilities.UserCustomClaims;

import net.openid.appauth.AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

/**
 * Class to perform OIDC request during login
 */
//TODO: Shashi,Handle backend issues in test case(Such as invalid client id)
public class PIMLoginManager {
    private String TAG = PIMLoginManager.class.getSimpleName();
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager mPimAuthManager;
    private LoggingInterface mLoggingInterface;
    private PIMLoginListener mPimLoginListener;
    private AppTaggingInterface mTaggingInterface;
    private PIMUserManager mPimUserManager;

    public PIMLoginManager(Context context,PIMOIDCConfigration pimoidcConfigration) {
        mPimoidcConfigration = pimoidcConfigration;
        mPimAuthManager = new PIMAuthManager(context);
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        mTaggingInterface = PIMSettingManager.getInstance().getTaggingInterface();
        mPimUserManager = PIMSettingManager.getInstance().getPimUserManager();
    }

     public Intent getAuthReqIntent(@NonNull PIMLoginListener pimLoginListener) throws ActivityNotFoundException {
        mPimLoginListener = pimLoginListener;
        String clientID = mPimoidcConfigration.getClientId();
        String redirectUrl = mPimoidcConfigration.getRedirectUrl();
        return mPimAuthManager.getAuthorizationRequestIntent(mPimoidcConfigration.getAuthorizationServiceConfiguration(), clientID, redirectUrl,createAdditionalParameterForLogin());
    }

    public boolean isAuthorizationSuccess(Intent intentData){
        return mPimAuthManager.isAuthorizationSuccess(intentData);
    }

    public void exchangeAuthorizationCode(@NonNull Intent dataIntent) {
        mPimAuthManager.performTokenRequest(dataIntent, new PIMTokenRequestListener() {
            @Override
            public void onTokenRequestSuccess() {
                mPimUserManager.requestUserProfile(mPimAuthManager.getAuthState(), new PIMUserProfileDownloadListener() {
                    @Override
                    public void onUserProfileDownloadSuccess() {
                        mPimUserManager.saveLoginFlowType(PIMUserManager.LOGIN_FLOW.DEFAULT);
                           mPimLoginListener.onLoginSuccess();
                    }

                    @Override
                    public void onUserProfileDownloadFailed(Error error) {
                        mPimLoginListener.onLoginFailed(error);
                    }
                }); //Request user profile on success of token request
            }

            @Override
            public void onTokenRequestFailed(Error error) {
                if (mPimLoginListener != null)
                    mPimLoginListener.onLoginFailed(error);
            }
        });
    }

    public AuthorizationRequest createAuthRequestUriForMigration(Map additionalParameter){
        return mPimAuthManager.createAuthRequestUriForMigration(additionalParameter);
    }

    public void exchangeAuthorizationCodeForMigration(AuthorizationRequest authorizationRequest, String authResponse, PIMUserMigrationListener pimUserMigrationListener){
        mPimAuthManager.performTokenRequest(authorizationRequest, authResponse, new PIMTokenRequestListener() {
            @Override
            public void onTokenRequestSuccess() {
                mLoggingInterface.log(DEBUG, TAG, "exchangeAuthorizationCodeForMigration success");
                mPimUserManager.requestUserProfile(mPimAuthManager.getAuthState(), new PIMUserProfileDownloadListener() {
                    @Override
                    public void onUserProfileDownloadSuccess() {
                        mPimUserManager.saveLoginFlowType(PIMUserManager.LOGIN_FLOW.MIGRATION);
                        pimUserMigrationListener.onUserMigrationSuccess();
                    }

                    @Override
                    public void onUserProfileDownloadFailed(Error error) {
                        pimUserMigrationListener.onUserMigrationFailed(error);
                    }
                });
            }

            @Override
            public void onTokenRequestFailed(Error error) {
                mLoggingInterface.log(DEBUG, TAG, "exchangeAuthorizationCodeForMigration Failed. Error : " + error.getErrDesc());
                pimUserMigrationListener.onUserMigrationFailed(error);
            }
        });
    }

    /**
     * Creates additional parameter for authorization request intent
     * @return map containing additional parameter in key-value pair
     */
    private Map<String, String> createAdditionalParameterForLogin() {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("claims", mPimoidcConfigration.getCustomClaims());
        AppTaggingInterface.PrivacyStatus privacyConsent = mTaggingInterface.getPrivacyConsent();
        boolean bool;
        bool = privacyConsent.equals(AppTaggingInterface.PrivacyStatus.OPTIN);
        parameter.put("analytics_consent", String.valueOf(bool));
        String urlString = "http://";
        String[] urlStringWithVisitorData = mTaggingInterface.getVisitorIDAppendToURL(urlString).split("=");
        parameter.put("adobe_mc",urlStringWithVisitorData[1] );

//        if (Analytics.getTrackingIdentifier() != null) {
//            parameter.put("analytics_adobe_mc", mTaggingInterface.getTrackingIdentifier());
//        } else {
//            mLoggingInterface.log(DEBUG, TAG, "ADBMobile tracking Identifier is not set.");
//        }
        parameter.put("ui_locales", PIMSettingManager.getInstance().getLocale());
        parameter.put("analytics_report_suite_id",new PIMOIDCConfigration().getrsID());
        mLoggingInterface.log(DEBUG, TAG, "Additional parameters : " + parameter.toString());
        return parameter;
    }
}

