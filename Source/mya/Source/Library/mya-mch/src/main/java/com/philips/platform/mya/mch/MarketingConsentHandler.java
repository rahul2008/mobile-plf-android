package com.philips.platform.mya.mch;

import java.util.ArrayList;
import java.util.List;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_UNKNOWN;

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
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {

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
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
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
