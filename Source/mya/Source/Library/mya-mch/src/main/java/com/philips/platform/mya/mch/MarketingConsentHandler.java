package com.philips.platform.mya.mch;

import static com.philips.platform.mya.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

import java.util.ArrayList;
import java.util.List;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.BackendConsent;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final User user;
    private final List<ConsentDefinition> definitions;
    private final AppInfraInterface appInfra;

    public MarketingConsentHandler(final User user, final List<ConsentDefinition> definitions, AppInfraInterface appInfra) {
        this.user = user;
        this.definitions = definitions == null ? new ArrayList<ConsentDefinition>() : definitions;
        this.appInfra = appInfra;
    }

    @Override
    public void checkConsents(CheckConsentsCallback callback) {

        try {
            final boolean receiveMarketingEmail = user.getReceiveMarketingEmail();
            List<Consent> marketingConsents = new ArrayList<>(definitions.size());
            final String currentLanguage = appInfra.getInternationalization().getBCP47UILocale();

            for (ConsentDefinition definition : definitions) {
                final Consent marketingConsent = createConsentFromDefinition(definition, toStatus(receiveMarketingEmail), currentLanguage);
                marketingConsents.add(marketingConsent);
            }

            callback.onGetConsentsSuccess(marketingConsents);
        } catch (Exception consentFailed) {
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN)); // TODO: Check with CatkError Codes
        }
    }

    @Override
    public void post(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        final String currentLanguage = appInfra.getInternationalization().getBCP47UILocale();

        user.updateReceiveMarketingEmail(new MarketingUpdateCallback(callback, definition, toStatus(status), currentLanguage), status);
    }

    private ConsentStatus toStatus(boolean recevieMarketingEmail) {
        return recevieMarketingEmail ? ConsentStatus.active : ConsentStatus.rejected;
    }

    private static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus, String consentLanguage) {
        final BackendConsent backendConsent = new BackendConsent(consentLanguage, consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    static class MarketingUpdateCallback implements UpdateUserDetailsHandler {
        private final PostConsentCallback callback;
        private final ConsentDefinition definition;
        private final Consent marketingConsent;

        MarketingUpdateCallback(PostConsentCallback callback, ConsentDefinition definition, ConsentStatus consentStatus, String consentLanguage) {
            this.callback = callback;
            this.definition = definition;
            marketingConsent = createConsentFromDefinition(definition, consentStatus, consentLanguage);
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
