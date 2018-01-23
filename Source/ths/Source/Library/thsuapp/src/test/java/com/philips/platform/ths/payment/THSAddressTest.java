/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class THSAddressTest {
    THSAddress mThsAddress;

    @Mock
    Address addressMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsAddress = new THSAddress();
    }

    @Test
    public void getAddress() throws Exception {
        mThsAddress.setAddress(addressMock);
        final Address address = mThsAddress.getAddress();
        assertNotNull(address);
        assertThat(address).isInstanceOf(Address.class);
    }

}