package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final Context context;
    private final List<ConsentDefinition> definitions;

    @VisibleForTesting
    User getUser() {
        return new User(context);
    }

    public MarketingConsentHandler(final Context context, final List<ConsentDefinition> definitions) {
        this.context = context;
        this.definitions = definitions == null ? new ArrayList<>() : definitions;
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
        getUser().refreshUser(new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                getMarketingConsentDefinition(callback);
            }

            @Override
            public void onRefreshUserFailed(int error) {
                getMarketingConsentDefinition(callback);
            }
        });
    }

    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        getUser().refreshUser(new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                getMarketingConsentDefinition(callback);
            }

            @Override
            public void onRefreshUserFailed(int error) {
                getMarketingConsentDefinition(callback);
            }
        });
    }

    private void getMarketingConsentDefinition(CheckConsentsCallback callback) {
        try {
            final boolean receiveMarketingEmail = getUser().getReceiveMarketingEmail();
            List<Consent> marketingConsents = new ArrayList<>(definitions.size());

            for (ConsentDefinition definition : definitions) {
                if (definition.getTypes().contains(URConsentProvider.USR_MARKETING_CONSENT)) {
                    final Consent marketingConsent = createConsentFromDefinition(definition, toStatus(receiveMarketingEmail));
                    marketingConsents.add(marketingConsent);
                    callback.onGetConsentsSuccess(marketingConsents);
                    return;
                }
            }
            callback.onGetConsentsFailed(new ConsentError(URConsentProvider.USR_MARKETING_CONSENT+ "Not Found", CONSENT_ERROR_UNKNOWN));
        } catch (Exception consentFailed) {
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN));
        }
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        getUser().updateReceiveMarketingEmail(new MarketingUpdateCallback(callback, definition, toStatus(status)), status);
    }

    private ConsentStatus toStatus(boolean recevieMarketingEmail) {
        return recevieMarketingEmail ? ConsentStatus.active : ConsentStatus.rejected;
    }

    private static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus) {
        final BackendConsent backendConsent = new BackendConsent(new Locale(definition.getLocale()), consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    static class MarketingUpdateCallback implements UpdateUserDetailsHandler {
        private final PostConsentCallback callback;
        private final ConsentDefinition definition;
        private final Consent marketingConsent;

        MarketingUpdateCallback(PostConsentCallback callback, ConsentDefinition definition, ConsentStatus consentStatus) {
            this.callback = callback;
            this.definition = definition;
            marketingConsent = createConsentFromDefinition(definition, consentStatus);
        }

        @Override
        public void onUpdateSuccess() {
            callback.onPostConsentSuccess(marketingConsent);
        }

        @Override
        public void onUpdateFailedWithError(int i) {
            callback.onPostConsentFailed(definition, new ConsentError("Error updating Marketing Consent", i));
        }
    }
}
