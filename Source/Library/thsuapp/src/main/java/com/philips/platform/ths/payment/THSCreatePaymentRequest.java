package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.billing.CreatePaymentRequest;

/**
 * Created by philips on 7/21/17.
 */

public class THSCreatePaymentRequest {
    public CreatePaymentRequest getCreatePaymentRequest() {
        return createPaymentRequest;
    }

    public void setCreatePaymentRequest(CreatePaymentRequest createPaymentRequest) {
        this.createPaymentRequest = createPaymentRequest;
    }

    CreatePaymentRequest createPaymentRequest;
}
