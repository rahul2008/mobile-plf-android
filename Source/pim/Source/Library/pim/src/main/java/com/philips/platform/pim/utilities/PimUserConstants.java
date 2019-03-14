package com.philips.platform.pim.utilities;

public enum PimUserConstants {
    FIRST_NAME("given_name"),
    LAST_NAME ("family_name"),
    GENDER("gender"),
    EMAIL("email"),
    MOBILE_NUMBER("phone_number"),
    BIRTHDAY("birthday"),
    EMAIL_VERIFIED("email_verified"),
    MOBILE_VERIFIED("phone_number_verified"),
    ADDRESS("address"),
    CREATED("created_at"),
    RECEIVE_MARKETING_EMAIL_TIMESTAMP("consent_email_marketing.timestamp"),
    RECEIVE_MARKETING_EMAIL("consent_email_marketing.given"),
    UUID("uuid"),
    SOCIAL_PROFILES("social_profiles");

    private final String text;

    PimUserConstants(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}


   