/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.SubscriptionUpdateRequest;

public class THSSubscriptionUpdateRequest {
    public SubscriptionUpdateRequest getSubscriptionUpdateRequest() {
        return subscriptionUpdateRequest;
    }

    public void setSubscriptionUpdateRequest(SubscriptionUpdateRequest subscriptionUpdateRequest) {
        this.subscriptionUpdateRequest = subscriptionUpdateRequest;
    }

    private SubscriptionUpdateRequest subscriptionUpdateRequest;
}
