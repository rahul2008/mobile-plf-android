/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient.response;

import com.philips.dhpclient.util.Objects;

import java.util.Map;

public final class DhpAuthenticationResponse extends DhpResponse {
    public final String accessToken;
    public final String refreshToken;
    public final Integer expiresIn;
    public final String userId;

    public DhpAuthenticationResponse(Map<String, Object> rawResponse) {
        this(null, null, null, null, rawResponse);
    }

    public DhpAuthenticationResponse(String accessToken, String refreshToken, Integer expiresIn, String userId, Map<String, Object> rawResponse) {
        super(rawResponse);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        DhpAuthenticationResponse that = (DhpAuthenticationResponse) o;
        return Objects.equals(accessToken, that.accessToken) &&
               Objects.equals(refreshToken, that.refreshToken) &&
               Objects.equals(expiresIn, that.expiresIn) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accessToken, refreshToken, expiresIn, userId);
    }

    @Override
    public String toString() {
        return "DhpAuthenticationResponse{" +
               "accessToken='" + accessToken + '\'' +
               ", refreshToken='" + refreshToken + '\'' +
               ", expiresIn=" + expiresIn +
               ", userId='" + userId + '\'' +
               "} " + super.toString();
    }
}
