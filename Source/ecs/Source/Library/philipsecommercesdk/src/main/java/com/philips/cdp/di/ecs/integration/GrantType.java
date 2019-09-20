package com.philips.cdp.di.ecs.integration;

public enum GrantType {

    JANRAIN("janrain"),
    REFRESH_TOKEN("refresh_token");

    public String getType() {
        return type;
    }

    String type;

    GrantType(String type) {
        this.type = type;
    }
}