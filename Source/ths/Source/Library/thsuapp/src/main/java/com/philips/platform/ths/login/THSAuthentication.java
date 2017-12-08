/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.login;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;

public class THSAuthentication {
    private Authentication authentication;

    public boolean needsToCompleteEnrollment() {
        return authentication.needsToCompleteEnrollment();
    }

    public ConsumerInfo getConsumerInfo() {
        return authentication.getConsumerInfo();
    }

    public boolean isCredentialsSystemGenerated() {
        return authentication.isCredentialsSystemGenerated();
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
