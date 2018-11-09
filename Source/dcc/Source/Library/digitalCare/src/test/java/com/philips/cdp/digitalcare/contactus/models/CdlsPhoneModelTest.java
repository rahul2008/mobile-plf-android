/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by philips on 10/30/18.
 */
public class CdlsPhoneModelTest {

    private CdlsPhoneModel cdlsPhoneModel;

    @Before
    public void setUp() throws Exception {
        cdlsPhoneModel = new CdlsPhoneModel();
    }

    @Test
    public void shouldTestSetPhoneNumberWhenBracketIsThere() throws Exception {
        String actualPhoneNumber = "(844)669 9935";
        String expectedPhoneNumber = "844 669 9935";
        cdlsPhoneModel.setPhoneNumber(actualPhoneNumber);
        Assert.assertEquals(cdlsPhoneModel.getPhoneNumber(),expectedPhoneNumber);
    }

    @Test
    public void shouldTestSetPhoneNumberWhenPhoneNumberIsProper() throws Exception {
        String actualPhoneNumber = "844 669 9935";
        String expectedPhoneNumber = "844 669 9935";
        cdlsPhoneModel.setPhoneNumber(actualPhoneNumber);
        Assert.assertEquals(cdlsPhoneModel.getPhoneNumber(),expectedPhoneNumber);
    }

    @Test
    public void shouldTestSetPhoneNumberWhenPhoneNumberIsWithCode() throws Exception {
        String actualPhoneNumber = "1-800-243-3050";
        String expectedPhoneNumber = "1-800-243-3050";
        cdlsPhoneModel.setPhoneNumber(actualPhoneNumber);
        Assert.assertEquals(cdlsPhoneModel.getPhoneNumber(),expectedPhoneNumber);
    }
}