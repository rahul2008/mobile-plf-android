package com.philips.platform.mya;

import java.util.Locale;

public class ConsentDefinition {
    private String localizedText;
    private String toolTip;
    private String consentType;
    private int version;
    private String locale;

    public ConsentDefinition(String localizedText, String toolTip, String consentType, int version, Locale locale) {
        this.localizedText = localizedText;
        this.toolTip = toolTip;
        this.consentType = consentType;
        this.version = version;
        this.locale = locale.toString().replace('_','-');
    }

    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String localizedText) {
        this.localizedText = localizedText;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getConsentType() {
        return consentType;
    }

    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
