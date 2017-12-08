/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class THSCreatePaymentRequestTest {
    THSCreatePaymentRequest mThsCreatePaymentRequest;

    @Mock
    CreatePaymentRequest createPaymentRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsCreatePaymentRequest = new THSCreatePaymentRequest();
    }

    @Test
    public void getCreatePaymentRequest() throws Exception {
        mThsCreatePaymentRequest.setCreatePaymentRequest(createPaymentRequest);
        final CreatePaymentRequest createPaymentRequest = mThsCreatePaymentRequest.getCreatePaymentRequest();
        assertNotNull(createPaymentRequest);
        assertThat(createPaymentRequest).isInstanceOf(CreatePaymentRequest.class);
    }

}