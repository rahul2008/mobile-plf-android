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

public final class DhpUserRegistrationResponse extends DhpResponse {
    public final String userId;

    public DhpUserRegistrationResponse(String userId, Map<String, Object> rawResponse) {
        super(rawResponse);
        this.userId = userId;
    }

    public DhpUserRegistrationResponse(String userId, String responseCode, Map<String, Object> rawResponse) {
        super(responseCode, rawResponse);
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
        DhpUserRegistrationResponse that = (DhpUserRegistrationResponse) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}
