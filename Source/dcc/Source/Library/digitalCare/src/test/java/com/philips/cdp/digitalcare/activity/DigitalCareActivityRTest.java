package com.philips.cdp.digitalcare.activity;

import android.app.Activity;
import android.content.Intent;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerCC;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
//import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

//import static junit.framework.Assert.assertNotNull;

/**
 * Created by philips on 6/29/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class DigitalCareActivityRTest {

    private Activity activity;

    @Before
    public void setUp() {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DigitalCareConstants.START_ANIMATION_ID, DigitalCareConstants.START_ANIMATION_ID);
        intent.putExtra(DigitalCareConstants.STOP_ANIMATION_ID, 0);
        intent.putExtra(DigitalCareConstants.SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);

        activity = Robolectric.buildActivity(DigitalCareActivity.class, intent).create().get();

    }

    @Ignore
    @Test
    public void testActivityNotNull() {
        Assert.assertNotNull(activity);
    }






}
