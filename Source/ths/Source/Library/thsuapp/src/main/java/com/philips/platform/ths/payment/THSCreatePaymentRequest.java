/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.billing.CreatePaymentRequest;


public class THSCreatePaymentRequest {
    CreatePaymentRequest createPaymentRequest;


    public CreatePaymentRequest getCreatePaymentRequest() {
        return createPaymentRequest;
    }

    public void setCreatePaymentRequest(CreatePaymentRequest createPaymentRequest) {
        this.createPaymentRequest = createPaymentRequest;
    }


}
