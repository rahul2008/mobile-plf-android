package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.adobe.mobile.Analytics;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.listeners.PIMUserProfileDownloadListener;
import com.philips.platform.pim.utilities.PIMConstants;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

//TODO : initilize instance and call login methods from Fragment(Done)
//TODO: Shashi,Handle backend issues in test case(Such as invalid client id)

public class PIMLoginManager implements PIMLoginListener, PIMUserProfileDownloadListener {
    private String TAG = PIMLoginManager.class.getSimpleName();
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager mPimAuthManager;
    private LoggingInterface mLoggingInterface;
    private PIMLoginListener mPimLoginListener;
    private AppTaggingInterface mTaggingInterface;

    public PIMLoginManager(PIMOIDCConfigration pimoidcConfigration) {
        mPimoidcConfigration = pimoidcConfigration;
        mPimAuthManager = new PIMAuthManager();
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        mTaggingInterface = PIMSettingManager.getInstance().getTaggingInterface();
    }

    public void oidcLogin(@NonNull Context context,@NonNull Bundle bundle,@NonNull PIMFragment pimFragment, @NonNull PIMLoginListener pimLoginListener) {
        mPimLoginListener = pimLoginListener;
        if (mPimoidcConfigration == null) {
            mLoggingInterface.log(DEBUG, TAG, "OIDC Login failed, Reason : PIMOIDCConfigration is null.");
        } else {
            Intent authReqIntent = mPimAuthManager.getAuthorizationRequestIntent(context, mPimoidcConfigration.getAuthorizationServiceConfiguration(),getClientId(), createAdditionalParameterForLogin(bundle));
            if (authReqIntent != null)
                pimFragment.startActivityForResult(authReqIntent, 100);
            else {
                mLoggingInterface.log(DEBUG, TAG, "OIDC Login failed, Reason : authReqIntent is null.");
            }
        }
    }

    public void exchangeAuthorizationCode(@NonNull Context context,@NonNull Intent dataIntent) {
            mPimAuthManager.performTokenRequest(context, dataIntent, this);
    }

    @Override
    public void onUserProfileDownloadSuccess() {
        mPimLoginListener.onLoginSuccess();
    }

    @Override
    public void onUserProfileDownloadFailed(Error error) {
        mPimLoginListener.onLoginFailed(error);
    }

    @Override
    public void onLoginSuccess() {
        PIMUserManager pimUserManager = PIMSettingManager.getInstance().getPimUserManager();
        pimUserManager.requestUserProfile(mPimAuthManager.getAuthState(), this);
    }

    @Override
    public void onLoginFailed(Error error) {
        if (mPimLoginListener != null)
            mPimLoginListener.onLoginFailed(error);
    }

    private Map<String, String> createAdditionalParameterForLogin(Bundle bundle) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("claims", bundle.getString(PIMConstants.PIM_KEY_CUSTOM_CLAIMS));
        parameter.put("cookie_consent", String.valueOf(mTaggingInterface.getPrivacyConsent()));//String.valueOf(Config.getPrivacyStatus() == MobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN));
        if (Analytics.getTrackingIdentifier() != null) {
            parameter.put("adobe_mc", mTaggingInterface.getTrackingIdentifier());
        } else {
            mLoggingInterface.log(DEBUG, TAG, "ADBMonbile tracking Identifier is not set.");
        }
        parameter.put("ui_locales", PIMSettingManager.getInstance().getLocale());
        mLoggingInterface.log(DEBUG,TAG,"Additional parameters : "+parameter.toString());
        return parameter;
    }

    // TODO: Get appinfra via settings manager or create constructor to inject what is required
    private String getClientId() {
        AppConfigurationInterface appConfigurationInterface = PIMSettingManager.getInstance().getAppInfraInterface().getConfigInterface();
        Object obj = appConfigurationInterface.getPropertyForKey(PIMConstants.CLIENT_ID, PIMConstants.GROUP_PIM, new AppConfigurationInterface.AppConfigurationError());
        if (obj != null) {
            return (String) obj;
        }
        return null;
    }


}

