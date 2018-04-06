package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

/**
 * Implement ConsentHandler Interface to receive marketing email consent on refresh user callback
 *
 * @since 2018.1.0
 */
public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final ConsentError NO_CONNECTION_ERROR = new ConsentError("There was no internet connection when posting marketing consent", ConsentError.CONSENT_ERROR_NO_CONNECTION);
    private AppInfraInterface appInfra;
    private final Context context;
    private final String TAG = MarketingConsentHandler.class.getSimpleName();

    /**
     * @param appInfra
     * @param context
     */
    public MarketingConsentHandler(AppInfraInterface appInfra, @NonNull final Context context) {
        this.appInfra = appInfra;
        this.context = context;
    }

    @VisibleForTesting
    User getUser() {
        return new User(context);
    }

    /**
     * @param consentType given type
     * @param callback    callback to be invoked after fetch
     */
    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        if (appInfra.getRestClient().isInternetReachable()) {
            getUser().refreshUser(new RefreshUserHandler() {
                @Override
                public void onRefreshUserSuccess() {
                    getMarketingConsentDefinition(consentType, callback);
                    RLog.d(TAG, "onRefreshUserSuccess ");
                }

                @Override
                public void onRefreshUserFailed(int error) {
                    getMarketingConsentDefinition(consentType, callback);
                    RLog.e(TAG, "onRefreshUserFailed ");
                }
            });
        } else {
            callback.onGetConsentsFailed(NO_CONNECTION_ERROR);
        }
    }

    /**
     * @param consentType given consent type
     * @param status      given status to store
     * @param version     given version
     * @param callback    callback to be invoked after store
     */
    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        if (appInfra.getRestClient().isInternetReachable()) {
            RLog.d(TAG, "storeConsentTypeState, So updateReceiveMarketingEmail ");
            getUser().updateReceiveMarketingEmail(new MarketingUpdateCallback(callback), status);
        } else {
            callback.onPostConsentFailed(NO_CONNECTION_ERROR);
        }
    }

    private void getMarketingConsentDefinition(String consentType, FetchConsentTypeStateCallback callback) {
        try {
            final boolean receiveMarketingEmail = getUser().getReceiveMarketingEmail();
            RLog.d(TAG, "getMarketingConsentDefinition : receiveMarketingEmail " + receiveMarketingEmail);
            if (consentType.equals(URConsentProvider.USR_MARKETING_CONSENT)) {
                RLog.d(TAG, "getMarketingConsentDefinition : onGetConsentsSuccess");
                //Keeping version 0 as Janrain is not providing but it should be janrain consent version
                callback.onGetConsentsSuccess(new ConsentStatus(toStatus(receiveMarketingEmail), 0));
                return;
            }
            RLog.e(TAG, "getMarketingConsentDefinition : onGetConsentsFailed");
            callback.onGetConsentsFailed(new ConsentError(URConsentProvider.USR_MARKETING_CONSENT + " Not Found", CONSENT_ERROR_UNKNOWN));
        } catch (Exception consentFailed) {
            RLog.e(TAG, "getMarketingConsentDefinition : onGetConsentsFailed Exception : " + consentFailed.getMessage());
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN));
        }
    }

    private ConsentStates toStatus(boolean recevieMarketingEmail) {
        final ConsentStates consentStates = recevieMarketingEmail ? ConsentStates.active : ConsentStates.rejected;
        RLog.d(TAG, "toStatus : " + consentStates);
        return consentStates;
    }

    static class MarketingUpdateCallback implements UpdateUserDetailsHandler {
        private final PostConsentTypeCallback callback;

        MarketingUpdateCallback(PostConsentTypeCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onUpdateSuccess() {
            RLog.d("MarketingUpdateCallback", "onUpdateSuccess ");
            callback.onPostConsentSuccess();
        }

        @Override
        public void onUpdateFailedWithError(int i) {
            RLog.d("MarketingUpdateCallback", "onUpdateFailedWithError : Error updating Marketing Consent ");
            callback.onPostConsentFailed(new ConsentError("Error updating Marketing Consent", i));
        }
    }
}
