/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.content.res.Resources;
import android.widget.TextView;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class HamburgerActivityTest {
    private HamburgerActivity hamburgerActivity = null;
    private TextView actionBarTitle = null;
    private Resources resource = null;
    private TestAppFrameworkApplication application = null;

    @Before
    public void setup() {
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().get();
        actionBarTitle = (TextView) hamburgerActivity.findViewById(R.id.af_actionbar_title);
        resource = hamburgerActivity.getResources();
        application = new TestAppFrameworkApplication();
        application.setTargetFlowManager();
    }

    @Test
    public void titleShouldContainValue() throws Exception {
        String title = resource.getString(com.philips.cdp.di.iap.R.string.app_name);
        hamburgerActivity.setTitle(title);

        assertNotNull(title);


//        assertThat("Show error for title field ", actionBarTitle.getError(), is(CoreMatchers.notNullValue()));
    }

//    @Test
//    public void titleShouldNotBeNull() throws Exception {
//        String title = null;
//        hamburgerActivity.setTitle(title);
//    }
}