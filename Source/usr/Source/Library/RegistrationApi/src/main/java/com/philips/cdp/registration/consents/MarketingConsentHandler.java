package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final Context context;
    private final AppInfraInterface appInfra;

    public MarketingConsentHandler(@NonNull final Context context, AppInfraInterface appInfraInterface) {
        this.context = context;
        this.appInfra = appInfraInterface;
    }

    @VisibleForTesting
    User getUser() {
        return new User(context);
    }

    private void getMarketingConsentDefinition(String consentType, FetchConsentTypeStateCallback callback) {
        try {
            final boolean receiveMarketingEmail = getUser().getReceiveMarketingEmail();

            if (consentType.equals(URConsentProvider.USR_MARKETING_CONSENT)) {
                callback.onGetConsentsSuccess(new ConsentStatus(toStatus(receiveMarketingEmail), 0));
                return;
            }
            callback.onGetConsentsFailed(new ConsentError(URConsentProvider.USR_MARKETING_CONSENT + " Not Found", CONSENT_ERROR_UNKNOWN));
        } catch (Exception consentFailed) {
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN));
        }
    }

    private ConsentStates toStatus(boolean recevieMarketingEmail) {
        return recevieMarketingEmail ? ConsentStates.active : ConsentStates.rejected;
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        getUser().refreshUser(new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                getMarketingConsentDefinition(consentType, callback);
            }

            @Override
            public void onRefreshUserFailed(int error) {
                getMarketingConsentDefinition(consentType, callback);
            }
        });
    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        getUser().updateReceiveMarketingEmail(new MarketingUpdateCallback(callback), status);
    }

    static class MarketingUpdateCallback implements UpdateUserDetailsHandler {
        private final PostConsentTypeCallback callback;

        MarketingUpdateCallback(PostConsentTypeCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onUpdateSuccess() {
            callback.onPostConsentSuccess();
        }

        @Override
        public void onUpdateFailedWithError(int i) {
            callback.onPostConsentFailed(new ConsentError("Error updating Marketing Consent", i));
        }
    }
}
