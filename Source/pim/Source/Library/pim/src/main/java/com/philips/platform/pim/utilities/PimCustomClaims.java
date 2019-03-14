package com.philips.platform.pim.utilities;

public enum PimCustomClaims {
    CREATED_AT("created_at"),
    SOURCES("sources"),
    RECEIVE_MARKETING_EMAIL_TIMESTAMP("consent_email_marketing.timestamp"),
    RECEIVE_MARKETING_EMAIL("consent_email_marketing.given"),
    DATA_TRANSFER_ACCEPTANCE_TIMESTAMP("consent_data_transfer.timestamp"),
    DATA_TRANSFER_ACCEPTANCE("consent_data_transfer.given"),
    DATA_USAGE_ACCEPTANCE("consent_data_usage.given"),
    DATA_USAGE_ACCEPTANCE_TIMESTAMP("consent_data_usage.timestamp"),
    UUID("uuid"),
    SOCIAL_PROFILES("social_profiles");

    private final String text;

    PimCustomClaims(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

}
