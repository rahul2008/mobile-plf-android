package com.philips.platform.pim.utilities;

public enum PimScopes {
    OPENID("openid"),
    PROFILE("profile"),
    ADDRESS("address"),
    EMAIL("email"),
    PHONE("phone");

    private final String text;

    PimScopes(String text){
        this.text = text;
    }

    public String getText(){
            return text;
    }
}
