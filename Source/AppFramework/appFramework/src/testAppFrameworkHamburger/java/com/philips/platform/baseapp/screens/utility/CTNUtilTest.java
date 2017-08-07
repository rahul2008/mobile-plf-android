package com.philips.platform.baseapp.screens.utility;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by admin on 07/08/17.
 */

public class CTNUtilTest {

    private CTNUtil ctnUtil;

    @Before
    public void setUp() {
        ctnUtil = new CTNUtil();
    }

    @Test
    public void getCTNForHK() {
        Assert.assertEquals("HX6322/04", ctnUtil.getCtnForCountry("HK"));
    }

    @Test
    public void getCTNForMO() {
        Assert.assertEquals("HX6322/04", ctnUtil.getCtnForCountry("MO"));
    }

    @Test
    public void getCTNForIN() {
        Assert.assertEquals("HX6311/07", ctnUtil.getCtnForCountry("IN"));
    }

    @Test
    public void getCTNForUS() {
        Assert.assertEquals("HX6321/02", ctnUtil.getCtnForCountry("US"));
    }

    @Test
    public void getCTNForCN() {
        Assert.assertEquals("HX6721/33", ctnUtil.getCtnForCountry("CN"));
    }

    @Test
    public void getCTNForOtherCountry() {
        Assert.assertEquals("HX6064/33", ctnUtil.getCtnForCountry("NL"));
    }

    @After
    public void tearDown() {
        ctnUtil = null;
    }
}
