package com.philips.cdp.di.ecs.model.payment;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ECSPaymentProvider implements Serializable {

    private String paymentProviderUrl;

    public String getWorldpayUrl() {
        return paymentProviderUrl;
    }
}
