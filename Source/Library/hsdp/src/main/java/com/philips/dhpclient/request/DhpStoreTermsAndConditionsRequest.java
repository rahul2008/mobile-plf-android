package com.philips.dhpclient.request;

public class DhpStoreTermsAndConditionsRequest {
    public final String applicationName;
    public final String documentId;
    public final String documentVersion;
    public final String countryCode;
    public final String consentCode;

    public DhpStoreTermsAndConditionsRequest(String applicationName, String documentId, String documentVersion, String countryCode, String consentCode) {
        this.applicationName = applicationName;
        this.documentId = documentId;
        this.documentVersion = documentVersion;
        this.countryCode = countryCode;
        this.consentCode = consentCode;
    }
}
