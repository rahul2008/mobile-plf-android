package com.philips.cdp.digitalcare.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.activity.DigitalCareActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowTelephonyManager;

import static org.robolectric.Shadows.shadowOf;

/**
 * Created by philips on 7/6/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class, sdk = 25)

public class UtilsRTest {

    private Utils utils;
    private DigitalCareActivity activity;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
        utils = new Utils();

    }

    @Test
    public void checkCountryChina() throws Exception
    {
        DigitalCareConfigManager.getInstance().setCountry("CN");
        Assert.assertTrue( Utils.isCountryChina());
    }

    @Test
    public void checkCountryNotChina() throws Exception
    {
        DigitalCareConfigManager.getInstance().setCountry("IN");
        Assert.assertFalse( Utils.isCountryChina());
    }

    @Test
    public void checkCountryNotNull() throws Exception
    {
        DigitalCareConfigManager.getInstance().setCountry("IN");
        Assert.assertNotNull( Utils.isCountryChina());
    }

    @Ignore
    @Test
    public void checkIsTablet() throws Exception
    {

        Assert.assertTrue( Utils.isTablet(activity));
    }

    @Test
    public void checkIsSimAvailable() throws Exception
    {
        Assert.assertTrue( utils.isSimAvailable(context));
    }

    @Test
    public void checkIsSimNotAvailable() throws Exception
    {
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ShadowTelephonyManager shadowTelephonyManager = shadowOf(telephonyManager);
        shadowTelephonyManager.setSimState(TelephonyManager.SIM_STATE_ABSENT);

        Assert.assertFalse( utils.isSimAvailable(context));
    }

    @Test
    public void checkIsTelephonyEnabled() throws Exception
    {
        Assert.assertTrue( utils.isTelephonyEnabled(context));
    }

    @Test
    public void checkIsTelephonyNotEnabled() throws Exception
    {
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ShadowTelephonyManager shadowTelephonyManager = shadowOf(telephonyManager);
        shadowTelephonyManager.setPhoneType( TelephonyManager.PHONE_TYPE_NONE );

        Assert.assertFalse( utils.isTelephonyEnabled(context));
    }

    @Test
    public void checkLoadWebPageContent() throws Exception
    {
        String webpageUrl = "www.google.com";

        //Assert.assertTrue( Utils..loadWebPageContent());
    }


}
