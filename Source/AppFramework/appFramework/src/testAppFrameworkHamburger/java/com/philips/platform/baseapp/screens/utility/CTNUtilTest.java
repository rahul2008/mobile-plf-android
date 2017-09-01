/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.utility;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CTNUtilTest {

    private CTNUtil ctnUtil;

    @Before
    public void setUp() {
        ctnUtil = new CTNUtil();
    }

    @Test
    public void getCTNWhenCountryIsNullTest() {
        Assert.assertEquals("HX6064/33", ctnUtil.getCtnForCountry(null));
    }
    @Test
    public void getCTNForHKTest() {
        Assert.assertEquals("HX6322/04", ctnUtil.getCtnForCountry("HK"));
    }

    @Test
    public void getCTNForMOTest() {
        Assert.assertEquals("HX6322/04", ctnUtil.getCtnForCountry("MO"));
    }

    @Test
    public void getCTNForINTest() {
        Assert.assertEquals("HX6311/07", ctnUtil.getCtnForCountry("IN"));
    }

    @Test
    public void getCTNForUSTest() {
        Assert.assertEquals("HD8645/47", ctnUtil.getCtnForCountry("US"));
    }

    @Test
    public void getCTNForCNTest() {
        Assert.assertEquals("HX6721/33", ctnUtil.getCtnForCountry("CN"));
    }

    @Test
    public void getCTNForOtherCountryTest() {
        Assert.assertEquals("HX6064/33", ctnUtil.getCtnForCountry("NL"));
    }

    @After
    public void tearDown() {
        ctnUtil = null;
    }
}
