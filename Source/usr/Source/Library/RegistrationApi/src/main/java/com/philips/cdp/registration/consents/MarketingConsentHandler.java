package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.appinfra.AppInfraInterface;
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

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR_UNKNOWN;
/**
 * Implement ConsentHandler Interface to receive marketing email consent on refresh user callback
 * @since 2018.1.0
 */

public class MarketingConsentHandler implements ConsentHandlerInterface {

    private final Context context;
    private final List<ConsentDefinition> definitions;
    private final AppInfraInterface appInfra;

    @VisibleForTesting
    User getUser() {
        return new User(context);
    }

    /**
     *
     * @param context
     * @param definitions list of consent definition
     * @param appInfraInterface instance of AppInfra
     */
    public MarketingConsentHandler(@NonNull final Context context, final List<ConsentDefinition> definitions, AppInfraInterface appInfraInterface) {
        this.context = context;
        this.definitions = definitions == null ? new ArrayList<>() : definitions;
        this.appInfra = appInfraInterface;
    }

    /**
     *
     * @param consentDefinition instance of consentDefinition
     * @param callback instance of CheckConsentsCallback
     */
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

    /**
     *
     * @param consentDefinitions list of consent definition
     * @param callback instance of CheckConsentsCallback
     */
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
            String currentLocale = appInfra.getInternationalization().getBCP47UILocale();

            for (ConsentDefinition definition : definitions) {
                if (definition.getTypes().contains(URConsentProvider.USR_MARKETING_CONSENT)) {
                    final Consent marketingConsent = createConsentFromDefinition(definition, toStatus(receiveMarketingEmail), currentLocale);
                    marketingConsents.add(marketingConsent);
                    callback.onGetConsentsSuccess(marketingConsents);
                    return;
                }
            }
            callback.onGetConsentsFailed(new ConsentError(URConsentProvider.USR_MARKETING_CONSENT + " Not Found", CONSENT_ERROR_UNKNOWN));
        } catch (Exception consentFailed) {
            callback.onGetConsentsFailed(new ConsentError(consentFailed.getLocalizedMessage(), CONSENT_ERROR_UNKNOWN));
        }
    }

    /**
     *
     * @param definition instance of ConsentDefinition
     * @param status status of consent
     * @param callback instance of PostConsentCallback
     */
    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        String currentLocale = appInfra.getInternationalization().getBCP47UILocale();
        getUser().updateReceiveMarketingEmail(new MarketingUpdateCallback(callback, definition, toStatus(status), currentLocale), status);
    }

    private ConsentStatus toStatus(boolean recevieMarketingEmail) {
        return recevieMarketingEmail ? ConsentStatus.active : ConsentStatus.rejected;
    }

    private static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus, String currentLocale) {
        final BackendConsent backendConsent = new BackendConsent(currentLocale, consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    private static class MarketingUpdateCallback implements UpdateUserDetailsHandler {
        private final PostConsentCallback callback;
        private final ConsentDefinition definition;
        private final Consent marketingConsent;

        MarketingUpdateCallback(PostConsentCallback callback, ConsentDefinition definition, ConsentStatus consentStatus, String currentLocale) {
            this.callback = callback;
            this.definition = definition;
            marketingConsent = createConsentFromDefinition(definition, consentStatus, currentLocale);
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
