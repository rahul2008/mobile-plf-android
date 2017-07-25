package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.SubscriptionUpdateRequest;

/**
 * Created by philips on 7/17/17.
 */

public class THSSubscriptionUpdateRequest {
    public SubscriptionUpdateRequest getSubscriptionUpdateRequest() {
        return subscriptionUpdateRequest;
    }

    public void setSubscriptionUpdateRequest(SubscriptionUpdateRequest subscriptionUpdateRequest) {
        this.subscriptionUpdateRequest = subscriptionUpdateRequest;
    }

    SubscriptionUpdateRequest subscriptionUpdateRequest;
}
