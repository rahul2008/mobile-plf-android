/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.providerslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;

public class THSProviderPojoTest {

    THSProviderPoJo providerPoJo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        providerPoJo = new THSProviderPoJo();
    }

    @Test
    public void testGetSet(){
        providerPoJo.setProviderImageURL("www.google.com");
        assertTrue(providerPoJo.getProviderImageURL().equals("www.google.com"));
        providerPoJo.setProviderName("TEST");
        assertTrue(providerPoJo.getProviderName().equals("TEST"));
        providerPoJo.setProviderPractise("Practice");
        assertTrue(providerPoJo.getProviderPractise().equals("Practice"));
        providerPoJo.setProviderRating(10);
        assertTrue(providerPoJo.getProviderRating() == 10);
    }
}
