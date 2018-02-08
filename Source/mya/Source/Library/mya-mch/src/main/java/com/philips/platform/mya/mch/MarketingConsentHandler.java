package com.philips.platform.mya.mch;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.mya.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

/**
 * Created by Entreco on 19/12/2017.
 */

public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final User user;
    private final List<ConsentDefinition> definitions;

    public MarketingConsentHandler(final User user, final List<ConsentDefinition> definitions) {
        this.user = user;
        this.definitions = definitions == null ? new ArrayList<ConsentDefinition>() : definitions;
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {

        try {
            final boolean receiveMarketingEmail = user.getReceiveMarketingEmail();
            List<Consent> marketingConsents = new ArrayList<>(definitions.size());

            for (ConsentDefinition definition : definitions) {
                final Consent marketingConsent = createConsentFromDefinition(definition, toStatus(receiveMarketingEmail));
                marketingConsents.add(marketingConsent);
            }

            callback.onGetConsentsSuccess(marketingConsents);
        } catch (Exception consentFailed) {
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN)); // TODO: Check with CatkError Codes
        }
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        user.updateReceiveMarketingEmail(new MarketingUpdateCallback(callback, definition, toStatus(status)), status);
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
