package com.philips.platform.csw.permission;

import android.support.annotation.Nullable;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.ConsentDefinition;

/**
 * Created by Entreco on 17/11/2017.
 */

public class ConsentView {

    private final ConsentDefinition definition;
    private boolean isLoading = true;
    @Nullable private Consent consent;

    ConsentView(final ConsentDefinition definition) {
        this.definition = definition;
    }

    public String getConsentText() {
        return definition.getText();
    }

    String getHelpText() {
        return definition.getHelpText();
    }

    public String getType() {
        return definition.getType();
    }

    public int getVersion() {
        return definition.getVersion();
    }

    ConsentView storeConsent(Consent consent) {
        this.consent = consent;
        this.isLoading = false;
        return this;
    }

    boolean isSwitchEnabled() {
        return consent != null && consent.getStatus().equals(ConsentStatus.active);
    }

    ConsentDefinition getDefinition() {
        return definition;
    }

    boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsentView that = (ConsentView) o;

        if (!definition.equals(that.definition)) return false;
        return consent != null ? consent.equals(that.consent) : that.consent == null;
    }

    @Override
    public int hashCode() {
        int result = definition.hashCode();
        result = 31 * result + (consent != null ? consent.hashCode() : 0);
        return result;
    }
}
