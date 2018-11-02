package com.philips.cdp.digitalcare.contactus.models;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.util.ContactUsUtils;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 10/30/18.
 */
@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class)
public class CdlsPhoneModelTest {

    CdlsPhoneModel cdlsPhoneModel;

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