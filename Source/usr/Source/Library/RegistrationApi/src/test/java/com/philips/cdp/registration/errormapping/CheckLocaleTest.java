package com.philips.cdp.registration.errormapping;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.datamigration.DataMigration;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/30/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class CheckLocaleTest {


    CheckLocale checkLocale;
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        checkLocale=new CheckLocale();
    }

    @Test
    public void checkLanguageIfLocaleExist() throws Exception {

        Assert.assertNotNull("en-US",checkLocale.checkLanguage("zh-HK"));
    }

    @Test
    public void checkLanguageIfLocaleDoesNotExist() throws Exception {

        Assert.assertNotNull("zh-PK",checkLocale.checkLanguage("zh-PK"));
    }

}