package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.Subscription;

/**
 * Created by philips on 7/17/17.
 */

public class THSSubscription {

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    Subscription subscription;


}
