package com.philips.amwelluapp.login;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;

public class PTHAuthentication {
    private Authentication authentication;

    public boolean needsToCompleteEnrollment(){
        return authentication.needsToCompleteEnrollment();
    }

    public ConsumerInfo getConsumerInfo(){
        return authentication.getConsumerInfo();
    }

    public boolean isCredentialsSystemGenerated(){
        return authentication.isCredentialsSystemGenerated();
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
