/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.billing.PaymentMethod;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class THSPaymentMethodTest {

    THSPaymentMethod mThsPaymentMethod;

    @Mock
    PaymentMethod paymentMethodMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsPaymentMethod = new THSPaymentMethod();
    }

    @Test
    public void getPaymentMethod() throws Exception {
        mThsPaymentMethod.setPaymentMethod(paymentMethodMock);
        final PaymentMethod paymentMethod = mThsPaymentMethod.getPaymentMethod();
        assertNotNull(paymentMethod);
        assertThat(paymentMethod).isInstanceOf(PaymentMethod.class);
    }

}