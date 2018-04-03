package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.ui.utils.RLog;
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

    private final Context context;
    private final String TAG = MarketingConsentHandler.class.getSimpleName();

    /**
     * @param context
     */
    public MarketingConsentHandler(@NonNull final Context context) {
        this.context = context;
    }

    @VisibleForTesting
    User getUser() {
        return new User(context);
    }

    private void getMarketingConsentDefinition(String consentType, FetchConsentTypeStateCallback callback) {
        try {
            final boolean receiveMarketingEmail = getUser().getReceiveMarketingEmail();
            RLog.d(TAG, "getMarketingConsentDefinition : receiveMarketingEmail " + receiveMarketingEmail);
            if (consentType.equals(URConsentProvider.USR_MARKETING_CONSENT)) {
                RLog.d(TAG, "getMarketingConsentDefinition : onGetConsentsSuccess");
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

    /**
     * @param consentType given type
     * @param callback    callback to be invoked after fetch
     */
    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        getUser().refreshUser(new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                RLog.d(TAG, "onRefreshUserSuccess ");
                getMarketingConsentDefinition(consentType, callback);
            }

            @Override
            public void onRefreshUserFailed(int error) {
                RLog.e(TAG, "onRefreshUserFailed ");
                getMarketingConsentDefinition(consentType, callback);
            }
        });
    }

    /**
     * @param consentType given consent type
     * @param status      given status to store
     * @param version     given version
     * @param callback    callback to be invoked after store
     */
    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        RLog.d(TAG, "storeConsentTypeState, So updateReceiveMarketingEmail ");
        getUser().updateReceiveMarketingEmail(new MarketingUpdateCallback(callback), status);
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
